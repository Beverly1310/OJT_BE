package com.example.ojt.service.typeJob;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.TypeJobRequest;
import com.example.ojt.model.dto.response.SuccessResponse;
import com.example.ojt.model.entity.TypeJob;
import com.example.ojt.repository.ITypeJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TypeJobService implements ITypeJobService {

    @Autowired
    private ITypeJobRepository typeJobRepository;

    @Override
    public Page<TypeJob> findAll(Pageable pageable, String search) {
        Page<TypeJob> typeJobs;
        if (search.isEmpty()) {
            typeJobs = typeJobRepository.findAll(pageable);
        } else {
            typeJobs = typeJobRepository.findAllByNameContains(search, pageable);
        }
        return typeJobs;
    }

    @Override
    public boolean addTypeJob(TypeJobRequest typeJobRequest) throws CustomException {
        try {
            TypeJob typeJob = new TypeJob();
            typeJob.setName(typeJobRequest.getName());
            typeJobRepository.save(typeJob);
            return true;
        } catch (Exception e) {
            throw new CustomException("Lỗi khi thêm TypeJob", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean updateTypeJob(TypeJobRequest typeJobRequest, Integer updateId) throws CustomException {
        try {
            Optional<TypeJob> existingJob = typeJobRepository.findById(updateId);
            if (existingJob.isPresent()) {
                TypeJob typeJob = existingJob.get();
                typeJob.setName(typeJobRequest.getName());
                typeJobRepository.save(typeJob);
                return true;
            } else {
                throw new CustomException("TypeJob not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new CustomException("Error updating TypeJob", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean deleteByIdTypeJob(Integer deleteId) throws CustomException {
        try {
            if (typeJobRepository.existsById(deleteId)) {
                typeJobRepository.deleteById(deleteId);
                return true;
            } else {
                throw new CustomException("TypeJob not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new CustomException("Error deleting TypeJob", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public SuccessResponse findById(Integer findId) throws CustomException {
        try {
            TypeJob typeJob = typeJobRepository.findById(findId)
                    .orElseThrow(() -> new CustomException("TypeJob not found", HttpStatus.NOT_FOUND));

            return SuccessResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Get TypeJob success")
                    .data(typeJob)
                    .build();
        } catch (Exception e) {
            throw new CustomException("Error finding TypeJob", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}