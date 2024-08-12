package com.example.ojt.service.typeJob;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.TypeCompanyRequest;
import com.example.ojt.model.dto.request.TypeJobRequest;
import com.example.ojt.model.dto.response.SuccessResponse;
import com.example.ojt.model.entity.TypeCompany;
import com.example.ojt.model.entity.TypeJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITypeJobService {
    Page<TypeJob> findAll(Pageable pageable, String search);
    boolean addTypeJob(TypeJobRequest typeJobRequest) throws CustomException;
    boolean updateTypeJob(TypeJobRequest typeJobRequest) throws CustomException;
    boolean deleteByIdTypeJob(Integer deleteId) throws CustomException;
    SuccessResponse findById(Integer findId) throws CustomException;
}
