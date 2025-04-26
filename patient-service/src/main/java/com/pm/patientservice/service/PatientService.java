package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {
    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients(){
        List<Patient> patients = patientRepository.findAll(); // findall() is a JPA method to create a query

        List<PatientResponseDTO>  patientResponseDOTs =
                patients.stream().map(PatientMapper::toDTO).toList();
        //  before replace with lamda:  List<PatientResponseDTO>  patientResponseDOTs =
                //patients.stream().map(patient -> PatientMapper.toDTO(patient)).toList();

        return patientResponseDOTs;
    }

}
