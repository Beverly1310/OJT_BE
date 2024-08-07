package com.example.ojt.service.company;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.EditCompanyRequest;
import com.example.ojt.model.entity.Company;

public interface ICompanyService {
    boolean update(EditCompanyRequest companyRequest) throws CustomException;
//    Company findById(Integer id) throws CustomException;
}
