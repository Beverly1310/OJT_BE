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
        // Kiểm tra xem tên TypeJob đã tồn tại chưa
        Optional<TypeJob> existingJob = typeJobRepository.findByName(typeJobRequest.getName());
        if (existingJob.isPresent()) {
            throw new CustomException("TypeJob name already exists", HttpStatus.CONFLICT);
        }

        // Nếu tên chưa tồn tại, tiếp tục thêm mới
        TypeJob typeJob = new TypeJob();
        typeJob.setName(typeJobRequest.getName());
        typeJobRepository.save(typeJob);
        return true;
    }


    @Override
    public boolean updateTypeJob(TypeJobRequest typeJobRequest) throws CustomException {
        Optional<TypeJob> existingJob = typeJobRepository.findById(typeJobRequest.getId());
        if (existingJob.isPresent()) {
            TypeJob typeJob = existingJob.get();

            // Kiểm tra xem tên mới có tồn tại và không phải của TypeJob hiện tại
            Optional<TypeJob> duplicateJob = typeJobRepository.findByName(typeJobRequest.getName());
            if (duplicateJob.isPresent() && !duplicateJob.get().getId().equals(typeJob.getId())) {
                throw new CustomException("TypeJob name already exists", HttpStatus.CONFLICT);
            }

            // Nếu tên hợp lệ, tiếp tục cập nhật
            typeJob.setName(typeJobRequest.getName());
            typeJobRepository.save(typeJob);
            return true;
        } else {
            throw new CustomException("TypeJob not found", HttpStatus.NOT_FOUND);
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