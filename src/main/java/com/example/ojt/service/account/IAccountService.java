package com.example.ojt.service.account;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.LoginAccountRequest;
import com.example.ojt.model.dto.request.RegisterAccountCandidate;
import com.example.ojt.model.dto.request.UpdateAccountCandidate;
import com.example.ojt.model.dto.response.JWTResponse;

public interface IAccountService {
    JWTResponse login(LoginAccountRequest loginAccountRequest) throws CustomException;

    boolean registerCandidate(RegisterAccountCandidate registerAccountCandidate) throws CustomException;
    boolean updateCandidate(UpdateAccountCandidate updateAccountCandidate) throws CustomException;
}
