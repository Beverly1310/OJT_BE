package com.example.ojt.controller;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.ChangePasswordRequest;
import com.example.ojt.model.dto.request.LoginAccountRequest;
import com.example.ojt.model.dto.request.PasswordChangeRequest;
import com.example.ojt.model.dto.request.PasswordRequestThroughEmail;
import com.example.ojt.model.dto.request.RegisterAccount;
import com.example.ojt.model.dto.request.RegisterAccountCompanyRequest;
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


    /**
     * đăng nhập admin
     *
     * @param loginAccountRequest
     * @return
     * @throws CustomException
     */
    @PostMapping("/admin/sign-in")
    public ResponseEntity<JWTResponse> loginadmin(@RequestBody LoginAccountRequest loginAccountRequest) throws CustomException {
        JWTResponse jwtResponse = accountService.loginadmin(loginAccountRequest);
        return ResponseEntity.ok(jwtResponse);
    }


    /**
     * đăng kí admin
     *
     * @param registerAccount
     * @return
     * @throws CustomException
     */
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
    public ResponseEntity<?> doRegisterCompany(@Valid @RequestBody RegisterAccountCompanyRequest registerAccount) throws
            CustomException {
        boolean check = accountService.registerCompany(registerAccount);
        if (check) {
            APIResponse response = new APIResponse(200, "Register successful, please verify account");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            throw new CustomException("Lack of compulsory registration information or invalid information.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    @PutMapping("/company/verify")
    public ResponseEntity<?> verifyCompanyOtp(@RequestParam String email, @RequestParam Integer otp) throws
            CustomException {
        if (accountService.companyVerify(email, otp)) {
            APIResponse response = new APIResponse(200, "Verify successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new CustomException("Otp in valid", HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/recoverPassword")
    public ResponseEntity<?> getPasswordFromEmail(@Valid @RequestBody PasswordRequestThroughEmail request) throws
            CustomException {
        try {
            accountService.requestPasswordThroughEmail(request);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "An email containing password has been sent to " + request.getEmail() + "! Please check your email!", ""));
        } catch (CustomException e) {
            return globalExceptionHandler.handleCustomException(e);
        }
    }

    /**
     * thay đổi mật khẩu admin
     *
     * @param changePasswordRequest
     * @return
     * @throws CustomException
     */
    @PostMapping("/admin/changepassword")
    public ResponseEntity<?> changeAdminPassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) throws
            CustomException {
        boolean check = accountService.changeAdminPassword(changePasswordRequest);
        if (check) {
            APIResponse response = new APIResponse(200, "Password changed successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new CustomException("Password change failed", HttpStatus.BAD_REQUEST);
        }
    }




    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeRequest request) throws
            CustomException {
        try {
            accountService.requestPasswordChange(request);
            return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "Password changed successfully!", ""));
        } catch (CustomException e) {
            return globalExceptionHandler.handleCustomException(e);

        }
    }
}



