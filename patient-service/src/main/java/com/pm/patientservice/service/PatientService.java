package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service //@Service 是一个注解，表示这个类是一个服务类（即包含业务逻辑的类）。
public class PatientService {
    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository){
        //这是构造函数，它在创建 PatientService 对象时会注入一个 PatientRepository，也就是数据库操作的工具，帮助获取患者信息。
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients(){
        //这是一个方法，它会从数据库中获取所有患者的信息。
        List<Patient> patients = patientRepository.findAll(); // findall() is a JPA method to create a query
        //这一行代码是通过 patientRepository.findAll() 从数据库中获取所有的患者记录。
        List<PatientResponseDTO>  patientResponseDOTs =
                patients.stream().map(PatientMapper::toDTO).toList();
        //  before replace with lamda:  List<PatientResponseDTO>  patientResponseDOTs =
                //patients.stream().map(patient -> PatientMapper.toDTO(patient)).toList();

        //通过流（stream）处理患者数据，
        // patients.stream() 表示将患者列表变成一个流，
        // map(PatientMapper::toDTO) 是用来将每个 Patient 对象转化为 PatientResponseDTO 对象，
        // 最后 toList() 会把流转换回列表。

        return patientResponseDOTs;
        //方法返回的是一个 PatientResponseDTO 类型的列表，这样前端或者调用者就能看到患者信息的格式化结果了。
    }

}
