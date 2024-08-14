package com.example.ojt.controller.admincontroller;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.LoginAccountRequest;
import com.example.ojt.model.dto.response.JWTResponse;
import com.example.ojt.service.account.IAccountService;
import com.example.ojt.service.candidate.ICandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/admin")

public class AdminController {
    @Autowired
    private ICandidateService candidateService;
    @GetMapping("/viewCandidateCV/{candidateId}")
    public ResponseEntity<?> viewCandidateCV(@PathVariable Integer candidateId) throws CustomException{
        return ResponseEntity.ok(candidateService.getCandidateCV(candidateId));
    }

    @GetMapping("viewCandidateInfo/{candidateId}")
    public ResponseEntity<?> viewCandidateBasicInformation(@PathVariable Integer candidateId) throws CustomException{
        return ResponseEntity.ok(candidateService.getBasicInfo(candidateId));
    }



}
