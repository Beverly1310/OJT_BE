package com.example.ojt.controller.admincontroller;

import com.example.ojt.exception.IdFormatException;
import com.example.ojt.service.account.IAccountService;
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

    @Autowired
    private IAccountService accountService;

    /**
     * lấy ds công ty
     * @param pageable
     * @return
     */
    @GetMapping("/companies")
    public ResponseEntity<?> findAllCompany(@PageableDefault Pageable pageable) {
        return companyService.getAllCompanies(pageable);
    }


    /**
     * xoa công ty
     * @param companyId
     * @return
     * @throws IdFormatException
     */
    @DeleteMapping("/companies/{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Integer companyId) throws IdFormatException {
        companyService.deleteCompany(companyId);
        return ResponseEntity.noContent().build();
    }


    /**
     * công ty nổi bật
     * @param companyId
     * @return
     */
    @PatchMapping("/companies/{companyId}")
    public ResponseEntity<Integer> changeOutstandingStatus(@PathVariable Integer companyId){
        return companyService.changeOutstandingStatus(companyId);
    }


    /**
     * duyệt công ty
     * @param companyId
     * @return
     */
    @PatchMapping("/company/browse/{companyId}")
    public ResponseEntity<Integer> changeStatusAcount(@PathVariable Integer companyId) {
        return accountService.  changeStatusAcount(companyId);
    }

    @GetMapping("/companies/count")
    public Long countCompany() {
        return companyService.countCompanies();
    }

}
