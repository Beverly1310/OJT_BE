package com.example.ojt.controller.admincontroller;

import com.example.ojt.exception.IdFormatException;
import com.example.ojt.service.company.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/admin")
public class CompanyAdminController {

    @Autowired
    private ICompanyService companyService;

    @GetMapping("/companies")
    public ResponseEntity<?> findAllCompany(@PageableDefault Pageable pageable) {
        return companyService.getAllCompanies(pageable);
    }

    @DeleteMapping("/companies/{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Integer companyId) throws IdFormatException {
        companyService.deleteCompany(companyId);
        return ResponseEntity.noContent().build();
    }
}
