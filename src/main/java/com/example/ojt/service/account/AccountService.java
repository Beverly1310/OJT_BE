package com.example.ojt.service.account;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.ChangePasswordRequest;
import com.example.ojt.model.dto.request.LoginAccountRequest;
import com.example.ojt.model.dto.request.RegisterAccount;
import com.example.ojt.model.dto.response.JWTResponse;
import com.example.ojt.model.entity.Account;
import com.example.ojt.model.entity.Role;
import com.example.ojt.model.entity.RoleName;
import com.example.ojt.repository.IAccountRepository;
import com.example.ojt.repository.IRoleRepository;
import com.example.ojt.security.jwt.JWTProvider;
import com.example.ojt.security.principle.AccountDetailsCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public JWTResponse login(LoginAccountRequest loginAccountRequest) throws CustomException {
        // Authenticate email and password
        Authentication authentication;
        try {
            authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginAccountRequest.getEmail(), loginAccountRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new CustomException("Email or password incorrect", HttpStatus.NOT_FOUND);
        }

        AccountDetailsCustom detailsCustom = (AccountDetailsCustom) authentication.getPrincipal();
        if (detailsCustom.getStatus() == 2) {
            throw new CustomException("Account has been blocked!", HttpStatus.FORBIDDEN);
        }

        String accessToken = jwtProvider.generateAccessToken(detailsCustom);

        return JWTResponse.builder()
                .email(detailsCustom.getEmail())
                .roleName(detailsCustom.getRoleName())
                .status(detailsCustom.getStatus())
                .accessToken(accessToken)
                .build();
    }

    @Override
    public boolean register(RegisterAccount registerAccount) throws CustomException {
        if (accountRepository.existsByEmail(registerAccount.getEmail())) {
            throw new CustomException("Email existed!", HttpStatus.CONFLICT);
        }
        if (!registerAccount.getPassword().equals(registerAccount.getConfirmPassword())) {
            throw new CustomException("Password do not match!", HttpStatus.BAD_REQUEST);
        }

        Role role = roleRepository.findByRoleName(RoleName.valueOf(registerAccount.getRoleName()))
                .orElseThrow(() -> new CustomException("Role not found", HttpStatus.NOT_FOUND));

        Account account = Account.builder()
                .email(registerAccount.getEmail())
                .password(passwordEncoder.encode(registerAccount.getPassword()))
                .status(1)
                .role(role)
                .build();

        accountRepository.save(account);
        return true;
    }

    @Override
    public JWTResponse loginadmin(LoginAccountRequest loginAccountRequest) throws CustomException {
        // Authenticate email and password
        Authentication authentication = null; //  lưu trữ thông tin chi tiết của người dùng đã được xác thực. đại diện cho người dùng hiện đang đăng nhập.
        try {
            authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(loginAccountRequest.getEmail(), loginAccountRequest.getPassword()));
            //sử dụng phương thức manager.authenticate() để xác thực người dùng dựa trên email và mật khẩu được cung cấp
            // tạo ra một đối tượng mới UsernamePasswordAuthenticationToken, đại diện cho thông tin xác thực (email và mật khẩu) của người dùng cần được xác thực.
        } catch (AuthenticationException e) {
            throw new CustomException("Email or password incorrect", HttpStatus.NOT_FOUND);
        }

        AccountDetailsCustom detailsCustom = (AccountDetailsCustom) authentication.getPrincipal();   //  // Principal : đại diện cho người dùng đã đăng nhập.
        if (detailsCustom.getStatus() == 2) {
            throw new CustomException("Account has been blocked!", HttpStatus.FORBIDDEN);
        }

        String accessToken = jwtProvider.generateAccessToken(detailsCustom); // tạo 1 token cho ng dùng đã xác thực.chứa tt cơ bản về ng dùng

        if (!Objects.equals(detailsCustom.getRoleName(), RoleName.ROLE_ADMIN.name())) {
            throw new CustomException("You are not an Admin!", HttpStatus.FORBIDDEN);
        }


        return JWTResponse.builder()
                .email(detailsCustom.getEmail())
                .roleName(detailsCustom.getRoleName())
                .status(detailsCustom.getStatus())
                .accessToken(accessToken)
                .build();
    }



    @Override
    public boolean registerAdmin(RegisterAccount registerAccount) throws CustomException {
        if (accountRepository.existsByEmail(registerAccount.getEmail())) {
            throw new CustomException("Email existed!", HttpStatus.CONFLICT);
        }
        if (!registerAccount.getPassword().equals(registerAccount.getConfirmPassword())) {
            throw new CustomException("Password do not match!", HttpStatus.BAD_REQUEST);
        }
        Account account = Account.builder()
                .email(registerAccount.getEmail())
                .password(passwordEncoder.encode(registerAccount.getPassword()))
                .status(1)
                .role(roleRepository.findByRoleName(RoleName.valueOf("ROLE_ADMIN")).orElseThrow(() -> new CustomException("Role not found", HttpStatus.NOT_FOUND)))
                .build();

        accountRepository.save(account);

        return true;
    }

    @Override
    public boolean changeAdminPassword(ChangePasswordRequest changePasswordRequest) throws CustomException {
        Account account = accountRepository.findByEmail(changePasswordRequest.getEmail())
                .orElseThrow(() -> new CustomException("Email not found", HttpStatus.NOT_FOUND));

        if (!Objects.equals(account.getRole().getRoleName(), RoleName.ROLE_ADMIN)) {
            throw new CustomException("Only admin can change the password using this endpoint", HttpStatus.FORBIDDEN);
        }

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), account.getPassword())) {
            throw new CustomException("Old password is incorrect", HttpStatus.BAD_REQUEST);
        }

        account.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        accountRepository.save(account);

        return true;
    }
}
