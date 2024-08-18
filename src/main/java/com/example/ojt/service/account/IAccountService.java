package com.example.ojt.service.account;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.ChangePasswordRequest;
import com.example.ojt.model.dto.request.LoginAccountRequest;

import com.example.ojt.model.dto.request.UpdateAccountCandidate;
import com.example.ojt.model.dto.request.PasswordChangeRequest;
import com.example.ojt.model.dto.request.PasswordRequestThroughEmail;
import com.example.ojt.model.dto.request.RegisterAccount;
import com.example.ojt.model.dto.request.RegisterAccountCompanyRequest;
import com.example.ojt.model.dto.response.JWTResponse;
import org.springframework.http.ResponseEntity;

public interface IAccountService {
    JWTResponse login(LoginAccountRequest loginAccountRequest) throws CustomException;

    boolean registerCandidate(RegisterAccount registerAccount) throws CustomException;

    boolean updateCandidate(UpdateAccountCandidate updateAccountCandidate) throws CustomException;


    JWTResponse loginadmin(LoginAccountRequest loginAccountRequest) throws CustomException;

    boolean registerAdmin(RegisterAccount registerAccount) throws CustomException;

    boolean changeAdminPassword(ChangePasswordRequest changePasswordRequest) throws CustomException;

    boolean registerCompany(RegisterAccountCompanyRequest registerAccount) throws CustomException;

    boolean accountVerify(String email, Integer otp) throws CustomException;


    void requestPasswordThroughEmail(PasswordRequestThroughEmail request) throws CustomException;

    void requestPasswordChange(PasswordChangeRequest request) throws CustomException;

    boolean resendOtp(String email) throws CustomException;

    ResponseEntity<Integer> changeStatusAcount(Integer companyId);


}
