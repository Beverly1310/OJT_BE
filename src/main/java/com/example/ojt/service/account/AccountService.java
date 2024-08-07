package com.example.ojt.service.account;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.LoginAccountRequest;
import com.example.ojt.model.dto.request.PasswordChangeRequest;
import com.example.ojt.model.dto.request.PasswordRequestThroughEmail;
import com.example.ojt.model.dto.request.RegisterAccount;
import com.example.ojt.model.dto.response.JWTResponse;
import com.example.ojt.model.entity.Account;
import com.example.ojt.model.entity.Role;
import com.example.ojt.model.entity.RoleName;
import com.example.ojt.repository.IAccountRepository;
import com.example.ojt.repository.IRoleRepository;
import com.example.ojt.security.jwt.JWTProvider;
import com.example.ojt.security.principle.AccountDetailsCustom;
import com.example.ojt.service.BackupPasswordGenerator;
import com.example.ojt.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private BackupPasswordGenerator passwordGenerator;

    @Override
    public JWTResponse login(LoginAccountRequest loginAccountRequest) throws CustomException {
        Account account = accountRepository.findByEmail(loginAccountRequest.getEmail())
                .orElseThrow(() -> new CustomException("Account not found!", HttpStatus.NOT_FOUND));
//        Kiểm tra role tài khoản
        boolean roleCheck;
        switch (loginAccountRequest.getRole()){
            case "candidate":
                roleCheck=account.getRole().getRoleName().equals(RoleName.ROLE_CANDIDATE);
                break;
            case "admin":
                roleCheck=account.getRole().getRoleName().equals(RoleName.ROLE_ADMIN);
                break;
            case "company":
                roleCheck=account.getRole().getRoleName().equals(RoleName.ROLE_COMPANY);
                break;
            default:
                throw new CustomException("Role not found!",HttpStatus.BAD_REQUEST);
        }
        if (roleCheck){
            //  Đặt lại mật khẩu nếu dùng mật khẩu dự phòng
            if (account.getBackupPassword() != null && passwordEncoder.matches(loginAccountRequest.getPassword(), account.getBackupPassword())) {
                account.setPassword(passwordEncoder.encode(loginAccountRequest.getPassword()));
                account.setBackupPassword(null);
                accountRepository.save(account);
            }

            Authentication authentication = null;
            try {
                authentication = manager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginAccountRequest.getEmail(), loginAccountRequest.getPassword()));
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
        } else {
            throw new CustomException("Account not found!", HttpStatus.NOT_FOUND);
        }

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
    public void requestPasswordThroughEmail(PasswordRequestThroughEmail request) throws CustomException {
        Account account = accountRepository.findByEmail(request.getEmail()).orElseThrow(() -> new CustomException("No account found with this email!", HttpStatus.NOT_FOUND));
        boolean roleCheck;
        switch (request.getRole()){
            case "candidate":
                roleCheck=account.getRole().getRoleName().equals(RoleName.ROLE_CANDIDATE);
                break;
            case "admin":
                roleCheck=account.getRole().getRoleName().equals(RoleName.ROLE_ADMIN);
                break;
            case "company":
                roleCheck=account.getRole().getRoleName().equals(RoleName.ROLE_COMPANY);
                break;
            default:
                throw new CustomException("Role not found!",HttpStatus.BAD_REQUEST);
        }
        if (roleCheck){
            String backupPassword = passwordGenerator.generate();
            account.setBackupPassword((passwordEncoder.encode(backupPassword)));
            accountRepository.save(account);
            String message = "Your recovery password is " + backupPassword + ". Do not share this message!";
            emailSenderService.sendEmail(request.getEmail(), "Password recovery", message);
        } else {
            throw new CustomException("No account found with this role!",HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void requestPasswordChange(PasswordChangeRequest request) throws CustomException {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()==null){
            throw new CustomException("Token not found!",HttpStatus.BAD_REQUEST);
        }
        AccountDetailsCustom accountDetailsCustom = (AccountDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account currentUser = accountRepository.findById(accountDetailsCustom.getId()).orElseThrow(()->new CustomException("Invalid token!",HttpStatus.NOT_FOUND));
        String currentPassword = currentUser.getPassword();
        if (!passwordEncoder.matches(request.getCurrentPassword(),currentPassword)) {
            throw new CustomException("Incorrect current password!", HttpStatus.BAD_REQUEST);
        } else {
            currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
            accountRepository.save(currentUser);
        }
    }
}
