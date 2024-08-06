package com.example.ojt.service.account;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.LoginAccountRequest;
import com.example.ojt.model.dto.request.RegisterAccount;
import com.example.ojt.model.dto.request.RegisterAccountCompanyRequest;
import com.example.ojt.model.dto.response.JWTResponse;

public interface IAccountService {
    JWTResponse login(LoginAccountRequest loginAccountRequest) throws CustomException;

    boolean register(RegisterAccount registerAccount) throws CustomException;
    boolean registerCompany(RegisterAccountCompanyRequest registerAccount) throws CustomException;

    boolean companyVerify(String email , Integer otp) throws CustomException;
}
