package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
//import com.pm.patientservice.grpc.BillingServiceGrpcClient;
//import com.pm.patientservice.kafka.KafkaProducer;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.kafka.KafkaProducer;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service //@Service 是一个注解，表示这个类是一个服务类（即包含业务逻辑的类）。
public class PatientService {
    private final PatientRepository patientRepository;
    private  final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;


    public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient, KafkaProducer kafkaProducer) {
        //这是构造函数，它在创建 PatientService 对象时会注入一个 PatientRepository，也就是数据库操作的工具，帮助获取患者信息。
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getPatients() {
        //这是一个方法，它会从数据库中获取所有患者的信息。
        List<Patient> patients = patientRepository.findAll(); // findall() is a JPA method to create a query
        //这一行代码是通过 patientRepository.findAll() 从数据库中获取所有的患者记录。
        List<PatientResponseDTO> patientResponseDOTs =
                patients.stream().map(PatientMapper::toDTO).toList();
        //  before replace with lamda:  List<PatientResponseDTO>  patientResponseDOTs =
        //patients.stream().map(patient -> PatientMapper.toDTO(patient)).toList();

        //通过流（stream）处理患者数据
        // patients.stream() 表示将患者列表变成一个流，
        // map(PatientMapper::toDTO) 是用来将每个 Patient 对象转化为 PatientResponseDTO 对象，
        // 最后 toList() 会把流转换回列表。

        return patientResponseDOTs;
        //方法返回的是一个 PatientResponseDTO 类型的列表，这样前端或者调用者就能看到患者信息的格式化结果了。
    }


    // service layer

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email is already" + patientRequestDTO.getEmail());
        }
        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(), newPatient.getName(),newPatient.getEmail());

        // add kafka
        kafkaProducer.sendEvent(newPatient);

        return PatientMapper.toDTO(newPatient);
    }

    //👉 **`PatientResponseDTO` 就是一个用来“给前端发送患者信息”的数据格式（对象）**。
    //详细一点讲：
    //- `DTO` 是 **Data Transfer Object** 的缩写，中文叫**数据传输对象**。
    //- `PatientResponseDTO` 里面通常只包含**前端需要看到的患者信息**，比如名字、年龄、地址等等。
    //- 它是从 `Patient`（数据库里的实体）转化过来的，但**不一定包含全部数据库字段**，而且可能做过处理（比如格式化日期、隐藏敏感信息）。
    //
    //为什么要用 `PatientResponseDTO` 而不是直接返回 `Patient`？
    //| 为什么 | 解释 |
    //|:---|:---|
    //| 安全性 | 比如数据库里有身份证号、密码，前端不应该拿到 |
    //| 灵活性 | 可以根据不同需求自定义返回内容 |
    //| 解耦 | 让数据库模型（Patient）和外部接口（API）分开，互不影响 |
    //| 方便维护 | 以后需求变了，改 DTO 就行，不动数据库里的实体 |

    //所以简单说：
    //- `Patient` 是数据库里的完整患者对象（很原始）。
    //- `PatientResponseDTO` 是**准备好给外面看的患者信息**（筛选后的、整理好的）。

    //（比如像“厨房的原材料” vs “餐桌上的菜”那样的比喻）


    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        //打log debug
        log.info("🚀 Received request to update patient with id: {}", id);
        log.info("Looking for patient with id: " + id); // 打印ID，确认请求进来的是正确的


        // 直接查数据库
        Optional<Patient> patientOptional = patientRepository.findById(id);

        log.info("🔍 Database query result - Patient found: {}", patientOptional.isPresent());
        // 打印是否找到Patient

        if (!patientOptional.isPresent()) {
            throw new PatientNotFoundException("Patient with id " + id + " not found");
        }

        // 如果查到了，继续走正常逻辑


        // 如果找到了，继续处理

        Patient patient = patientRepository.findById(id).orElseThrow(()->new PatientNotFoundException("Patient not found with ID:"+ id));

        log.info("✅ Found patient name: {}", patient.getName());

        // check if there is any patient with same email but with different id
        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),id)) {
            //检查是否有其他患者(id)已经使用了这个 email（除了当前正在更新的这个患者自己）
            throw new EmailAlreadyExistsException("A patient with this email is already" + patientRequestDTO.getEmail());
        }
        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);
    }

    public void deletePatient (UUID id) {
        patientRepository.deleteById(id);
    }
}


