package com.example.ojt.controller;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.LevelJobRequest;
import com.example.ojt.model.dto.response.SuccessResponse;
import com.example.ojt.service.levelJob.ILevelJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api.myservice.com/v1/level-job")
public class LevelJobController {
    @Autowired
    private ILevelJobService levelJobService;

    @GetMapping
    public ResponseEntity<?> findAll (@PageableDefault(page = 0, size = 3, sort = "id", direction = Sort.Direction.ASC)Pageable pageable,
                                      @RequestParam(defaultValue = "")String search,
        @RequestParam(defaultValue = "id") String sort,
    @RequestParam(defaultValue = "ASC") String direction){
    Pageable sortPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),Sort.by(Sort.Direction.fromString(direction),sort));
    return ResponseEntity.ok().body(levelJobService.findAll(sortPageable,search));
    }

    @PostMapping
    public ResponseEntity<?> addLevelJob(@RequestBody @Valid LevelJobRequest levelJobRequest) {
        try {
            boolean isAdded = levelJobService.addLevelJob(levelJobRequest);
            if (isAdded) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Tạo Level Job thành công");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tạo được LevelJob");
            }
        } catch (CustomException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }
    @PutMapping
    public ResponseEntity<?> updateLevelJob(@RequestBody LevelJobRequest levelJobRequest) {
        try {
            boolean isUpdated = levelJobService.updateLevelJob(levelJobRequest);
            if (isUpdated) {
                return ResponseEntity.ok().body("Cập nhật Level Job thành công");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không cập nhật được LevelJob");
            }
        } catch (CustomException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLevelJob(@PathVariable Integer id) {
        try {
            boolean isDeleted = levelJobService.deleteByIdLevelJob(id);
            if (isDeleted) {
                return ResponseEntity.ok().body("Xóa Level Job thành công");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không xóa được LevelJob");
            }
        } catch (CustomException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLevelJobById(@PathVariable Integer id) {
        try {
            SuccessResponse response = levelJobService.findById(id);
            return ResponseEntity.ok().body(response);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }
}
