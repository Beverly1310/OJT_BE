package com.example.ojt.service.levelJob;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.LevelJobRequest;
import com.example.ojt.model.dto.request.TypeCompanyRequest;
import com.example.ojt.model.dto.response.SuccessResponse;
import com.example.ojt.model.entity.LevelJob;
import com.example.ojt.model.entity.TypeCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ILevelJobService {
    Page<LevelJob> findAll(Pageable pageable, String search);
    boolean addLevelJob(LevelJobRequest levelJobRequest) throws CustomException;
    boolean updateLevelJob(LevelJobRequest levelJobRequest) throws CustomException;
    boolean deleteByIdLevelJob(Integer deleteId) throws CustomException;
    SuccessResponse findById(Integer findId) throws CustomException;
}
