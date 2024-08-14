package com.example.ojt.service.company;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.EditCompanyRequest;
import com.example.ojt.model.dto.response.CompanyResponse;
import com.example.ojt.model.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICompanyService {
    boolean update(EditCompanyRequest companyRequest) throws CustomException;
    Page<CompanyResponse> findAllCompanies(Pageable pageable, String location, String search);
    CompanyResponse findById(Integer id) throws CustomException;
    List<CompanyResponse> findCompaniesByTypeCompany(Integer companyId);
}

