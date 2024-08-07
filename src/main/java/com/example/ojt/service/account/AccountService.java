package com.example.ojt.service.account;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.ChangePasswordRequest;
import com.example.ojt.model.dto.request.LoginAccountRequest;
import com.example.ojt.model.dto.request.PasswordChangeRequest;
import com.example.ojt.model.dto.request.PasswordRequestThroughEmail;
import com.example.ojt.model.dto.request.RegisterAccount;
import com.example.ojt.model.dto.request.RegisterAccountCompanyRequest;
import com.example.ojt.model.dto.response.JWTResponse;
import com.example.ojt.model.dto.response.MailBody;
import com.example.ojt.model.entity.*;
import com.example.ojt.repository.*;
import com.example.ojt.security.jwt.JWTProvider;
import com.example.ojt.security.principle.AccountDetailsCustom;


import com.example.ojt.service.company.EmailService;

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
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;

import java.util.Objects;

@Service
@Transactional
public class AccountService implements IAccountService {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private IAddressCompanyRepository addressCompanyRepository;
    @Autowired
    private ILocationRepository locationRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ICompanyRepository companyRepository;
    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private IRoleRepository roleRepository;



    @Autowired

    private EmailService emailService;

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
    @Transactional
    public boolean registerCompany(RegisterAccountCompanyRequest registerAccount) throws CustomException {
        if (accountRepository.existsByEmail(registerAccount.getEmailCompany())) {
            throw new CustomException("Email existed!", HttpStatus.CONFLICT);
        }
        if (companyRepository.existsByPhone(registerAccount.getPhone())){
            throw new CustomException("Phone existed!", HttpStatus.CONFLICT);
        }
        if (!registerAccount.getPassword().equals(registerAccount.getConfirmPassword())) {
            throw new CustomException("Password do not match!", HttpStatus.BAD_REQUEST);
        }
        Role role = roleRepository.findByRoleName(RoleName.valueOf(registerAccount.getRoleName()))
                .orElseThrow(() -> new CustomException("Role not found", HttpStatus.NOT_FOUND));
        Integer otp = otpGenerator();
        Account account = Account.builder()
                .email(registerAccount.getEmailCompany())
                .password(passwordEncoder.encode(registerAccount.getPassword()))
                .otp(otp)
                .status(0)
                .role(role)
                .build();

        // Save account to get the ID
        accountRepository.save(account);

        Company company = Company.builder()
                .name(registerAccount.getName())  // Lấy tên từ yêu cầu đăng ký
                .createdAt(new Date())
                .account(account)
                .followers(0)
                .size(0)
                .emailCompany(account.getEmail())
                .phone(registerAccount.getPhone())
                .build();

        AddressCompany addressCompany = AddressCompany.builder()
                .company(company)
                .location(locationRepository.findById(registerAccount.getLocationId()).orElseThrow(()-> new CustomException("City not found", HttpStatus.NOT_FOUND)))
                .createdAt(new Date())
                .status(1)
                .build();

        companyRepository.save(company);
        addressCompanyRepository.save(addressCompany);

        emailService.sendSimpleMessage(new MailBody(account.getEmail(),"giangpc7@gmail.com","Your otp is: "+otp));
        return true;
    }

    @Override
    public boolean companyVerify(String email, Integer otp) throws CustomException {
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new CustomException("account not found", HttpStatus.NOT_FOUND) );
        if (otp.equals( account.getOtp())){
            account.setStatus(1);
            account.setOtp(null);
        }else {
            throw new CustomException("Invalid OTP" , HttpStatus.BAD_REQUEST);
        }
        return true;
    }


    //    https://www.creativefabrica.com/wp-content/uploads/2022/08/03/Phoenix-Logo-of-Mythological-Bird-Graphics-35417559-1-1-580x387.jpg
private Integer otpGenerator() {
    Random random = new Random();
    return random.nextInt(100_000, 999_999);
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


        public void requestPasswordThroughEmail (PasswordRequestThroughEmail request) throws CustomException {
            Account account = accountRepository.findByEmail(request.getEmail()).orElseThrow(() -> new CustomException("No account found with this email!", HttpStatus.NOT_FOUND));
            boolean roleCheck;
            switch (request.getRole()) {
                case "candidate":
                    roleCheck = account.getRole().getRoleName().equals(RoleName.ROLE_CANDIDATE);
                    break;
                case "admin":
                    roleCheck = account.getRole().getRoleName().equals(RoleName.ROLE_ADMIN);
                    break;
                case "company":
                    roleCheck = account.getRole().getRoleName().equals(RoleName.ROLE_COMPANY);
                    break;
                default:
                    throw new CustomException("Role not found!", HttpStatus.BAD_REQUEST);
            }
            if (roleCheck) {
                String backupPassword = passwordGenerator.generate();
                account.setBackupPassword((passwordEncoder.encode(backupPassword)));
                accountRepository.save(account);
                String message = "Your recovery password is " + backupPassword + ". Do not share this message!";
                emailSenderService.sendEmail(request.getEmail(), "Password recovery", message);
            } else {
                throw new CustomException("No account found with this role!", HttpStatus.BAD_REQUEST);
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
