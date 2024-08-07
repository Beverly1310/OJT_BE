package com.example.ojt.controller;

import com.example.ojt.advice.APIControllerAdvice;
import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.EduCandidateAddReq;
import com.example.ojt.model.dto.request.RegisterAccountCandidate;
import com.example.ojt.model.dto.request.UpdateAccountCandidate;
import com.example.ojt.model.dto.response.APIResponse;
import com.example.ojt.model.dto.response.DataResponse;
import com.example.ojt.model.entity.EducationCandidate;
import com.example.ojt.service.CandidateService;
import com.example.ojt.service.ICandidateService;
import com.example.ojt.service.account.IAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/candidate")
@RequiredArgsConstructor
public class CandidateController {
    private final IAccountService accountService;
    private final ICandidateService candidateService;

    @PutMapping("/update/account")
    public ResponseEntity<?> updateAccount(@Valid @ModelAttribute UpdateAccountCandidate updateAccountCandidate) throws CustomException {
        boolean check = accountService.updateCandidate(updateAccountCandidate);
        if (check) {
            APIResponse response = new APIResponse(200, "Update account success");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            throw new CustomException("Lack of compulsory registration information or invalid information.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    @PostMapping("/education")
    public  ResponseEntity<?> addEducation(@Valid @RequestBody EduCandidateAddReq eduCandidateAddReq) throws CustomException {
        boolean check = candidateService.addEducation(eduCandidateAddReq);
        if (check) {
            APIResponse response = new APIResponse(200, "Create education success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new CustomException("Create Edu fail", HttpStatus.BAD_REQUEST);
        }
    }
   @GetMapping("/education")
    public ResponseEntity<?> getEducation(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                          @RequestParam(required = false) String direction,
                                          @RequestParam(defaultValue = "") String search) throws CustomException {
       Page<EducationCandidate> educationCandidates = candidateService.getEducationCandidates(pageable,search,direction);
       APIResponse response = new APIResponse(200, "Get education success");
       return new ResponseEntity<>(new DataResponse<>(response, educationCandidates), HttpStatus.OK);
   }
   @GetMapping("/education/{id}")
    public ResponseEntity<?> getEducationById(@PathVariable Integer id) throws CustomException {
        EducationCandidate educationCandidate = candidateService.getEducationCandidate(id);
        APIResponse response = new APIResponse(200, "Get education success");
        return new ResponseEntity<>(new DataResponse<>(response, educationCandidate), HttpStatus.OK);
   }
   @PutMapping("/education/")
    public ResponseEntity<?> updateEducation(@Valid @RequestBody UpdateAccountCandidate updateAccountCandidate) throws CustomException {
        boolean check = accountService.updateCandidate(updateAccountCandidate);
        if (check) {
            APIResponse response = new APIResponse(200, "Update education success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
            throw new CustomException("Update education fail", HttpStatus.BAD_REQUEST);
        }
   }
   @DeleteMapping("/education/{id}")
    public ResponseEntity<?> deleteEducation(@PathVariable Integer id) throws CustomException {
        boolean check = candidateService.deleteEducationCandidate(id);
        if (check) {
            APIResponse response = new APIResponse(200, "Delete education success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new CustomException("Delete education fail", HttpStatus.BAD_REQUEST);
        }
   }
}
