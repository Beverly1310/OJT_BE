package com.example.ojt.controller;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.ChangePasswordRequest;
import com.example.ojt.model.dto.request.LoginAccountRequest;
import com.example.ojt.model.dto.request.RegisterAccount;
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

    /**
     * đăng nhập admin
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

    /**
     * thay đổi mật khẩu admin
     * @param changePasswordRequest
     * @return
     * @throws CustomException
     */
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
}
