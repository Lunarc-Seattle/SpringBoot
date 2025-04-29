package com.pm.patientservice.controller;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.dto.validators.CreatePatientValidationGroup;
import com.pm.patientservice.service.PatientService;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.hibernate.boot.model.internal.XMLContext;
import org.springframework.http.ResponseEntity;// HTTP response handling (ResponseEntity),
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

//PatientService for business logic,
// validation annotations (@Valid),

// and Spring Web annotations (@RestController, @RequestMapping, @GetMapping, etc.).

@RestController
//@RestController: This annotation is a combination of @Controller and @ResponseBody.
// It tells Spring that this class will handle HTTP requests and return data (usually in JSON).
@RequestMapping("/patients") // this patient controller will handle all the requests starts with "/patients"
// http"//localhost:4000/patients  all requests will be handled like this
public class PatientController {
    // dependency injection  Dependency Injection
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
    //Here, you are using dependency injection to ！！ inject the PatientService into the PatientController.
    //！！！！This allows you to call business logic methods from the PatientService to interact with your patient data.

    @GetMapping
    // This handles HTTP GET requests for the /patients endpoint. It retrieves a list of patients.
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        List<PatientResponseDTO> patients = patientService.getPatients();
        return ResponseEntity.ok().body(patients);
        //This returns an HTTP response with status 200 OK and the list of patients in the response body.
    }

    @PostMapping
    //This handles HTTP POST requests to create a new patient.
    // The body of the request is expected to be a JSON that will be converted to a PatientRequestDTO object.
    public ResponseEntity<PatientResponseDTO> createPatient(@Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        //@Valid: This annotation ensures that the incoming patientRequestDTO is validated. It will check any constraints (e.g., not null, size limits) defined in the PatientRequestDTO class.
        //@RequestBody: This tells Spring to map the incoming JSON request body into the PatientRequestDTO object.
        PatientResponseDTO patientResponseDTO = patientService.createPatient(patientRequestDTO);
        //Calls the service to create a new patient using the provided data.
        return ResponseEntity.ok().body(patientResponseDTO);
        //Returns an HTTP 200 OK status with the patientResponseDTO (the newly created patient data) in the response body.
    }

    // localhost:4000/patients/123-123-123-123-12-3
    // @ will convert/123-123-123-123-12-3 into id
    @PutMapping("/{id}")
    // 这个注解告诉 Spring Boot：这是一个 处理 HTTP PUT 请求 的方法。
    ///{id} 表示 URL 路径里要带一个参数，比如请求是 PUT /patients/123e4567-e89b-12d3-a456-426614174000，那么 id 就是 UUID 类型的 "123e4567-e89b-12d3-a456-426614174000"。
    //PUT 一般用于“更新”某个资源，比如更新某个病人的信息。


    // ！！！这里是更好的输出提示信息：-----------------------
    //比如 @Validated({Default.class}）
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id, @Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO){
        //@PathVariable UUID id	从 URL 中提取 id，比如上面的 "123e4..."
        //@RequestBody PatientRequestDTO patientRequestDTO	从请求的 JSON 正文中提取出患者的新信息（例如：新的名字、年龄等）
        //ResponseEntity<PatientResponseDTO>	表示返回一个 HTTP 响应，其中的 body 是一个格式化好的患者信息 DTO
        PatientResponseDTO patientResponseDTO = patientService.updatePatient(id,patientRequestDTO);
        //这行调用了你自己写的业务逻辑层 patientService 里的 updatePatient() 方法。
        //它的作用是：根据传入的 ID，去数据库查找该患者，然后根据 patientRequestDTO 里的内容去更新它，并返回更新后的结果。

        return ResponseEntity.ok().body(patientResponseDTO);
        //返回一个 HTTP 200 OK 响应
        //响应体是更新后的患者信息（即 PatientResponseDTO 对象），这会自动变成 JSON 发回前端。

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient (@PathVariable UUID id){
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
