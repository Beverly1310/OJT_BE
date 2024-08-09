package com.example.ojt.service.levelJob;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.LevelJobRequest;
import com.example.ojt.model.dto.response.SuccessResponse;
import com.example.ojt.model.entity.LevelJob;
import com.example.ojt.repository.ILevelJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LevelJobService implements ILevelJobService{
    @Autowired
    private ILevelJobRepository levelJobRepository;
    @Override
    public Page<LevelJob> findAll(Pageable pageable, String search) {
        Page<LevelJob> levelJobs;
        if (search.isEmpty()){
            levelJobs = levelJobRepository.findAll(pageable);
        }else {
            levelJobs = levelJobRepository.findAllByNameContains(search,pageable);
        }
        return levelJobs;
    }

    @Override
    public boolean addLevelJob(LevelJobRequest levelJobRequest) throws CustomException {
        Optional<LevelJob> existingJob = levelJobRepository.findByName(levelJobRequest.getName());
        if (existingJob.isPresent()) {
            throw new CustomException("LevelJob name already exists", HttpStatus.CONFLICT);
        }

        LevelJob levelJob = new LevelJob();
        levelJob.setName(levelJobRequest.getName());
        levelJobRepository.save(levelJob);
        return true;
    }

    @Override
    public boolean updateLevelJob(LevelJobRequest levelJobRequest) throws CustomException {
        Optional<LevelJob> existingJob = levelJobRepository.findById(levelJobRequest.getId());
        if (existingJob.isPresent()) {
            LevelJob levelJob = existingJob.get();

            // Kiểm tra xem tên mới có tồn tại và không phải của LevelJob hiện tại
            Optional<LevelJob> duplicateJob = levelJobRepository.findByName(levelJobRequest.getName());
            if (duplicateJob.isPresent() && !duplicateJob.get().getId().equals(levelJob.getId())) {
                throw new CustomException("LevelJob name already exists", HttpStatus.CONFLICT);
            }

            levelJob.setName(levelJobRequest.getName());
            levelJobRepository.save(levelJob);
            return true;
        } else {
            throw new CustomException("LevelJob not found", HttpStatus.NOT_FOUND);
        }
    }



    @Override
    public boolean deleteByIdLevelJob(Integer deleteId) throws CustomException {
        try {
            if (levelJobRepository.existsById(deleteId)) {
                levelJobRepository.deleteById(deleteId);
                return true;
            } else {
                throw new CustomException("LevelJob not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new CustomException("Error deleting LevelJob", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public SuccessResponse findById(Integer findId) throws CustomException {
        try {
            LevelJob levelJob = levelJobRepository.findById(findId)
                    .orElseThrow(() -> new CustomException("LevelJob not found", HttpStatus.NOT_FOUND));

            return SuccessResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Get LevelJob success")
                    .data(levelJob)
                    .build();
        } catch (Exception e) {
            throw new CustomException("Error finding LevelJob", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
