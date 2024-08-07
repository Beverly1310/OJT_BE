package com.example.ojt.controller;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.LoginAccountRequest;
import com.example.ojt.model.dto.request.PasswordChangeRequest;
import com.example.ojt.model.dto.request.PasswordRequestThroughEmail;
import com.example.ojt.model.dto.request.RegisterAccount;
import com.example.ojt.model.dto.response.APIResponse;
import com.example.ojt.model.dto.response.JWTResponse;
import com.example.ojt.model.dto.response.SuccessResponse;
import com.example.ojt.service.account.IAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api.myservice.com/v1/auth")
public class AuthController {
    @Autowired
    private IAccountService accountService;
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;
    @PostMapping("/sign-in")
    public ResponseEntity<?> login(@Valid @RequestBody LoginAccountRequest loginAccountRequest) {
        try {
            JWTResponse response = accountService.login(loginAccountRequest);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Login successful", response));
        } catch (CustomException ex) {
            return globalExceptionHandler.handleCustomException(ex);
        }
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

    @PostMapping("/recoverPassword")
    public ResponseEntity<?> getPasswordFromEmail(@Valid @RequestBody PasswordRequestThroughEmail request) throws CustomException {
        try {
            accountService.requestPasswordThroughEmail(request);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "An email containing password has been sent to " + request.getEmail() + "! Please check your email!", ""));
        } catch (CustomException e) {
            return globalExceptionHandler.handleCustomException(e);
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeRequest request) throws CustomException {
        try {
            accountService.requestPasswordChange(request);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Password changed successfully!", ""));
        } catch (CustomException e) {
            return globalExceptionHandler.handleCustomException(e);
        }
    }
}
