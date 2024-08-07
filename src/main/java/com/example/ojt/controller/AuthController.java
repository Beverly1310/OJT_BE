package com.example.ojt.controller;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.LoginAccountRequest;
import com.example.ojt.model.dto.request.RegisterAccount;
import com.example.ojt.model.dto.request.RegisterAccountCompanyRequest;
import com.example.ojt.model.dto.response.APIResponse;
import com.example.ojt.model.dto.response.JWTResponse;
import com.example.ojt.service.account.IAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/auth")
public class AuthController {
    @Autowired
    private IAccountService accountService;
    @PostMapping("/sign-in")
    public ResponseEntity<JWTResponse> doLogin(@Valid @RequestBody LoginAccountRequest loginAccountRequest) throws Exception {
        JWTResponse jwtResponse = accountService.login(loginAccountRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> doRegister(@Valid @RequestBody RegisterAccount registerAccount) throws CustomException {
        boolean check = accountService.register(registerAccount);
        if (check) {
            APIResponse response = new APIResponse(200, "Register successful");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            throw new CustomException("Lack of compulsory registration information or invalid information.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/company/sign-up")
    public ResponseEntity<?> doRegisterCompany(@Valid @RequestBody RegisterAccountCompanyRequest registerAccount) throws CustomException {
        boolean check = accountService.registerCompany(registerAccount);
        if (check) {
            APIResponse response = new APIResponse(200, "Register successful, please verify account");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            throw new CustomException("Lack of compulsory registration information or invalid information.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping("/company/verify")
    public ResponseEntity<?> verifyCompanyOtp(@RequestParam String email, @RequestParam Integer otp) throws CustomException {
        if (accountService.companyVerify(email,otp)){
            APIResponse response = new APIResponse(200, "Verify successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
            throw new CustomException("Otp in valid", HttpStatus.BAD_REQUEST);
        }
    }



}
