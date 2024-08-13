package com.example.ojt.service.company;

import com.example.ojt.exception.CustomException;
import com.example.ojt.exception.IdFormatException;
import com.example.ojt.model.dto.request.EditCompanyRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ICompanyService {
    boolean update(EditCompanyRequest companyRequest) throws CustomException;

    ResponseEntity<?> getAllCompanies(Pageable pageable);
//    Company findById(Integer id) throws CustomException;

    void deleteCompany(Integer id) throws IdFormatException;
}
