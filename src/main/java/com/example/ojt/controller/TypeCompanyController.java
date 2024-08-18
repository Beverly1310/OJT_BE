package com.example.ojt.controller;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.TypeCompanyRequest;
import com.example.ojt.model.dto.response.SuccessResponse;
import com.example.ojt.model.entity.TypeCompany;
import com.example.ojt.service.typeCompany.ITypeCompanyService;
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
@RequestMapping("/api.myservice.com/v1/type-company")
public class TypeCompanyController {
    @Autowired
    private ITypeCompanyService typeCompanyService;
    @GetMapping
    public ResponseEntity<?> findAll
            (@PageableDefault(page = 0,size =  3, sort = "id" , direction = Sort.Direction.ASC)Pageable pageable,
                @RequestParam(defaultValue = "") String search,
        @RequestParam(defaultValue = "id") String sort,
        @RequestParam(defaultValue = "ASC") String direction){
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.fromString(direction), sort)
        );
        return ResponseEntity.ok().body(typeCompanyService.findAll(sortedPageable, search));

    }

    @PostMapping
    public ResponseEntity<?> addTypeCompany(@RequestBody @Valid TypeCompanyRequest typeCompanyRequest) {
        try {
            boolean isAdded = typeCompanyService.addTypeCompany(typeCompanyRequest);
            if (isAdded) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Create type company success");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create type company");
            }
        } catch (CustomException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateTypeCompany( @RequestBody @Valid TypeCompanyRequest typeCompanyRequest) {
        try {
            boolean isUpdated = typeCompanyService.updateTypeCompany(typeCompanyRequest);
            if (isUpdated) {
                return ResponseEntity.ok().body("Update type company success");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update type company");
            }
        } catch (CustomException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTypeCompany(@PathVariable Integer id) {
        try {
            boolean isDeleted = typeCompanyService.deleteByIdTypeCompany(id);
            if (isDeleted) {
                return ResponseEntity.ok().body("Delete type company success");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete type company");
            }
        } catch (CustomException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTypeCompanyById(@PathVariable Integer id) {
        try {

            SuccessResponse response = typeCompanyService.findById(id);
            return ResponseEntity.ok().body(response);

        } catch (CustomException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }


}
