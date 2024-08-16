package com.example.ojt.controller;


import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.TypeJobRequest;
import com.example.ojt.model.dto.response.SuccessResponse;
import com.example.ojt.service.typeJob.ITypeJobService;
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
@RequestMapping("/api.myservice.com/v1/type-job")
public class TypeJobController {

    @Autowired
    private ITypeJobService typeJobService;

    @GetMapping
    public ResponseEntity<?> findAll(@PageableDefault(page = 0, size = 3, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                     @RequestParam(defaultValue = "") String search,
                                     @RequestParam(defaultValue = "id") String sort,
                                     @RequestParam(defaultValue = "ASC") String direction) {
        Pageable sortPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.fromString(direction), sort));
        return ResponseEntity.ok().body(typeJobService.findAll(sortPageable, search));
    }

    @PostMapping
    public ResponseEntity<?> addTypeJob(@RequestBody @Valid TypeJobRequest typeJobRequest) {
        try {
            boolean isAdded = typeJobService.addTypeJob(typeJobRequest);
            if (isAdded) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Tạo Type Job thành công");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tạo được TypeJob");
            }
        } catch (CustomException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateTypeJob(@RequestBody @Valid TypeJobRequest typeJobRequest) {
        try {
            boolean isUpdated = typeJobService.updateTypeJob(typeJobRequest);
            if (isUpdated) {
                return ResponseEntity.ok().body("Cập nhật Type Job thành công");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không cập nhật được TypeJob");
            }
        } catch (CustomException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTypeJob(@PathVariable Integer id) {
        try {
            boolean isDeleted = typeJobService.deleteByIdTypeJob(id);
            if (isDeleted) {
                return ResponseEntity.ok().body("Xóa Type Job thành công");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không xóa được TypeJob");
            }
        } catch (CustomException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTypeJobById(@PathVariable Integer id) {
        try {
            SuccessResponse response = typeJobService.findById(id);
            return ResponseEntity.ok().body(response);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }
}
