package com.pm.patientservice.controller;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;// HTTP response handling (ResponseEntity),
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientRequestDTO patientRequestDTO) {
        //@Valid: This annotation ensures that the incoming patientRequestDTO is validated. It will check any constraints (e.g., not null, size limits) defined in the PatientRequestDTO class.
        //@RequestBody: This tells Spring to map the incoming JSON request body into the PatientRequestDTO object.
        PatientResponseDTO patientResponseDTO = patientService.createPatient(patientRequestDTO);
        //Calls the service to create a new patient using the provided data.
        return ResponseEntity.ok().body(patientResponseDTO);
        //Returns an HTTP 200 OK status with the patientResponseDTO (the newly created patient data) in the response body.
    }
}
