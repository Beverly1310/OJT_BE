package com.example.ojt.service.typeCompany;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.LocationRequest;
import com.example.ojt.model.dto.request.TypeCompanyRequest;
import com.example.ojt.model.dto.response.SuccessResponse;
import com.example.ojt.model.entity.Location;
import com.example.ojt.model.entity.TypeCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITypeCompanyService {
    Page<TypeCompany> findAll(Pageable pageable, String search);
    boolean addTypeCompany(TypeCompanyRequest typeCompanyRequest) throws CustomException;
    boolean updateTypeCompany(TypeCompanyRequest typeCompanyRequest) throws CustomException;
    boolean deleteByIdTypeCompany(Integer deleteId) throws CustomException;
    SuccessResponse findById(Integer findId) throws CustomException;
}
