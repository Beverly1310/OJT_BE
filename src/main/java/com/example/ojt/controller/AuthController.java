package com.example.ojt.controller;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.*;
import com.example.ojt.model.dto.response.APIResponse;
import com.example.ojt.model.dto.response.JWTResponse;
import com.example.ojt.model.dto.response.SuccessResponse;
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
    public ResponseEntity<?> doLogin(@Valid @RequestBody LoginAccountRequest loginAccountRequest) throws Exception {
        JWTResponse response = accountService.login(loginAccountRequest);
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Login successful", response));
    }

    @PostMapping("/candidate/sign-up")
    public ResponseEntity<?> doRegister(@Valid @RequestBody RegisterAccount registerAccountCandidate) throws CustomException {
        boolean check = accountService.registerCandidate(registerAccountCandidate);
        if (check) {
            APIResponse response = new APIResponse(200, "Register successful, please verify your account");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new CustomException("Lack of compulsory registration information or invalid information.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/admin/sign-in")
    public ResponseEntity<JWTResponse> loginadmin(@RequestBody LoginAccountRequest loginAccountRequest) throws CustomException {
        JWTResponse jwtResponse = accountService.loginadmin(loginAccountRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<?> registerAdmin(@RequestBody @Valid RegisterAccount registerAccount) throws CustomException {
        boolean check = accountService.registerAdmin(registerAccount);
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

    @PutMapping("/verify")
    public ResponseEntity<?> verifyCompanyOtp(@RequestParam String email, @RequestParam Integer otp) throws CustomException {
        if (accountService.accountVerify(email, otp)) {
            APIResponse response = new APIResponse(200, "Verify successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new CustomException("Otp invalid", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/recoverPassword")
    public ResponseEntity<?> getPasswordFromEmail(@Valid @RequestBody PasswordRequestThroughEmail request) throws CustomException {
        accountService.requestPasswordThroughEmail(request);
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "An email containing the password has been sent to " + request.getEmail() + "! Please check your email!", ""));
    }

    @PostMapping("/admin/changepassword")
    public ResponseEntity<?> changeAdminPassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) throws CustomException {
        boolean check = accountService.changeAdminPassword(changePasswordRequest);
        if (check) {
            APIResponse response = new APIResponse(200, "Password changed successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new CustomException("Password change failed", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeRequest request) throws CustomException {
        accountService.requestPasswordChange(request);
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Password changed successfully!", ""));
    }

    @PutMapping("/resendotp")
    public ResponseEntity<?> resendOtp(@RequestParam String email) throws CustomException {
        boolean check = accountService.resendOtp(email);
        if (check) {
            APIResponse response = new APIResponse(200, "Otp has been sent to your email successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new CustomException("Resend otp failed", HttpStatus.BAD_REQUEST);
        }
    }
}
