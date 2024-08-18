package com.example.ojt.service.company;

import com.example.ojt.exception.CustomException;
import com.example.ojt.exception.IdFormatException;
import com.example.ojt.model.dto.request.EditCompanyRequest;

import com.example.ojt.model.dto.response.CandidateInfoRes;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import com.example.ojt.model.dto.response.CompanyResponse;
import org.springframework.data.domain.Page;


import java.util.List;

public interface ICompanyService {
    boolean update(EditCompanyRequest companyRequest) throws CustomException;

    ResponseEntity<?> getAllCompanies(Pageable pageable);
//    Company findById(Integer id) throws CustomException;

    void deleteCompany(Integer id) throws IdFormatException;

    CompanyResponse findCurrentCompany() throws CustomException;

    Page<CompanyResponse> findAllCompanies(Pageable pageable, String location, String search);
    CompanyResponse findById(Integer id) throws CustomException;


    ResponseEntity<Integer> changeOutstandingStatus(Integer companyId);

    List<CompanyResponse> findCompaniesByTypeCompany(Integer companyId);


    Long countCompanies();
    CandidateInfoRes getCandidateInfoById(Integer candidateId) throws CustomException;

}

