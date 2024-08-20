package com.example.ojt.service.account;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.*;
import com.example.ojt.model.dto.response.JWTResponse;
import com.example.ojt.model.entity.Account;
import com.example.ojt.model.entity.Candidate;
import com.example.ojt.model.entity.RoleName;
import com.example.ojt.repository.IAccountRepository;
import com.example.ojt.repository.ICandidateRepository;
import com.example.ojt.repository.IRoleRepository;
import com.example.ojt.model.dto.response.MailBody;
import com.example.ojt.model.entity.*;
import com.example.ojt.repository.*;
import com.example.ojt.security.jwt.JWTProvider;
import com.example.ojt.security.principle.AccountDetailsCustom;


import com.example.ojt.service.company.EmailService;

import com.example.ojt.service.BackupPasswordGenerator;
import com.example.ojt.service.EmailSenderService;

import com.example.ojt.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private ICandidateRepository candidateRepository;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private BackupPasswordGenerator passwordGenerator;


    public static AccountDetailsCustom getCurrentUser() {
        return (AccountDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public JWTResponse login(LoginAccountRequest loginAccountRequest) throws CustomException {
        Account account = accountRepository.findByEmail(loginAccountRequest.getEmail())
                .orElseThrow(() -> new CustomException("Account not found!", HttpStatus.NOT_FOUND));
//        Kiểm tra role tài khoản
        boolean roleCheck;
        switch (loginAccountRequest.getRole()) {
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
            //  Đặt lại mật khẩu nếu dùng mật khẩu dự phòng
            if (account.getBackupPassword() != null && passwordEncoder.matches(loginAccountRequest.getPassword(), account.getBackupPassword())) {
                account.setPassword(passwordEncoder.encode(loginAccountRequest.getPassword()));
                accountRepository.save(account);
            }

            Authentication authentication = null;
            try {
                authentication = manager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginAccountRequest.getEmail(), loginAccountRequest.getPassword()));
            } catch (AuthenticationException e) {
                throw new CustomException("Sai email hoặc mật khẩu!", HttpStatus.NOT_FOUND);
            }

            AccountDetailsCustom detailsCustom = (AccountDetailsCustom) authentication.getPrincipal();
            if (detailsCustom.getStatus() == 2) {
                throw new CustomException("Tài khoản bị khóa!", HttpStatus.UNAUTHORIZED);
            }

            if (detailsCustom.getStatus() == 0) {
                throw new CustomException("Tài khoản chưa được xác thực!", HttpStatus.UNAUTHORIZED);
            }

            String accessToken = jwtProvider.generateAccessToken(detailsCustom);
            return JWTResponse.builder()
                    .email(detailsCustom.getEmail())
                    .avatar(detailsCustom.getAvatar())
                    .name(detailsCustom.getName())
                    .roleName(detailsCustom.getRoleName())
                    .status(detailsCustom.getStatus())
                    .accessToken(accessToken)
                    .build();
        } else {
            throw new CustomException("Không tìm thấy tài khoản!", HttpStatus.NOT_FOUND);
        }

    }


    @Override
    @Transactional
    public boolean registerCandidate(RegisterAccount registerAccountCandidate) throws CustomException {
        if (accountRepository.existsByEmail(registerAccountCandidate.getEmail())) {
            throw new CustomException("Email existed!", HttpStatus.CONFLICT);
        }
        if (!registerAccountCandidate.getPassword().equals(registerAccountCandidate.getConfirmPassword())) {
            throw new CustomException("Password do not match!", HttpStatus.BAD_REQUEST);
        }
        Integer otp = otpGenerator();
        Account account = Account.builder()
                .name(registerAccountCandidate.getName())
                .email(registerAccountCandidate.getEmail())
                .password(passwordEncoder.encode(registerAccountCandidate.getPassword()))
                .otp(otp)
                .status(0)
                .role(roleRepository.findByRoleName(RoleName.valueOf("ROLE_CANDIDATE")).orElseThrow(() -> new CustomException("Role not found", HttpStatus.NOT_FOUND)))
                .build();
        Candidate candidate = Candidate.builder()
                .name(account.getName())
                .account(account)
                .status(1)
                .createdAt(new Date())
                .avatar("https://png.pngtree.com/png-vector/20220608/ourmid/pngtree-man-avatar-isolated-on-white-background-png-image_4891418.png")
                .build();
        emailService.sendSimpleMessage(new MailBody(account.getEmail(), "Verify account", "Your otp is: " + otp));

        candidateRepository.save(candidate);
        accountRepository.save(account);
        return true;
    }


    @Override
    @Transactional
    public boolean registerCompany(RegisterAccountCompanyRequest registerAccount) throws CustomException {
        if (accountRepository.existsByEmail(registerAccount.getEmailCompany())) {
            throw new CustomException("Email existed!", HttpStatus.CONFLICT);
        }
        if (companyRepository.existsByPhone(registerAccount.getPhone())) {
            throw new CustomException("Phone existed!", HttpStatus.CONFLICT);
        }
        if (!registerAccount.getPassword().equals(registerAccount.getConfirmPassword())) {
            throw new CustomException("Password do not match!", HttpStatus.BAD_REQUEST);
        }

        Integer otp = otpGenerator();
        Account account = Account.builder()
                .email(registerAccount.getEmailCompany())
                .password(passwordEncoder.encode(registerAccount.getPassword()))
                .otp(otp)
                .status(0)
                .role(roleRepository.findByRoleName(RoleName.valueOf("ROLE_COMPANY")).orElseThrow(() -> new CustomException("Role not found", HttpStatus.NOT_FOUND)))
                .build();

        // Save account to get the ID
        accountRepository.save(account);

        Company company = Company.builder()
                .name(registerAccount.getName())
                .createdAt(new Date())
                .account(account)
                .followers(0)
                .size(0)
                .emailCompany(account.getEmail())
                .phone(registerAccount.getPhone())
                .build();

        AddressCompany addressCompany = AddressCompany.builder()
                .company(company)
                .location(locationRepository.findById(registerAccount.getLocationId()).orElseThrow(() -> new CustomException("City not found", HttpStatus.NOT_FOUND)))
                .createdAt(new Date())
                .status(1)
                .build();
        emailService.sendSimpleMessage(new MailBody(account.getEmail(), "giangpc7@gmail.com", "Your otp is: " + otp));
        companyRepository.save(company);
        addressCompanyRepository.save(addressCompany);


        return true;
    }

    @Override
    public boolean updateCandidate(UpdateAccountCandidate updateAccountCandidate) throws CustomException {
        Candidate candidate = candidateRepository.findByAccountId(getCurrentUser().getId()).orElseThrow(() -> new CustomException("Candidate not found", HttpStatus.NOT_FOUND));
        if (updateAccountCandidate.getAboutMe() != null && !updateAccountCandidate.getAboutMe().isBlank()) {
            candidate.setAboutme(updateAccountCandidate.getAboutMe());
        }
        if (updateAccountCandidate.getAddress() != null && !updateAccountCandidate.getAddress().isBlank()) {
            candidate.setAddress(updateAccountCandidate.getAddress());
        }
        if (updateAccountCandidate.getAvatar() != null && !updateAccountCandidate.getAvatar().isEmpty()) {
            candidate.setAvatar(uploadService.uploadFileToServer(updateAccountCandidate.getAvatar()));
        }
        if (updateAccountCandidate.getBirthDay() != null) {
            candidate.setBirthday(updateAccountCandidate.getBirthDay());
        }
        if (updateAccountCandidate.getGender() != null) {
            candidate.setGender(updateAccountCandidate.getGender());
        }
        if (updateAccountCandidate.getLinkGit() != null && !updateAccountCandidate.getLinkGit().isBlank()) {
            candidate.setLinkGit(updateAccountCandidate.getLinkGit());
        }
        if ((updateAccountCandidate.getLinkLinkedin() != null && !updateAccountCandidate.getLinkLinkedin().isBlank())) {
            candidate.setLinkLinkedin(updateAccountCandidate.getLinkLinkedin());
        }
        if (updateAccountCandidate.getName() != null && !updateAccountCandidate.getName().isBlank()) {
            candidate.setName(updateAccountCandidate.getName());
        }
        if (updateAccountCandidate.getPhone() != null && !updateAccountCandidate.getPhone().isBlank()) {
            candidate.setPhone(updateAccountCandidate.getPhone());
        }
        if (updateAccountCandidate.getPosition() != null && !updateAccountCandidate.getPosition().isBlank()) {
            candidate.setPosition(updateAccountCandidate.getPosition());
        }
        candidate.setUpdatedAt(new Date());
        candidateRepository.save(candidate);
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


    public void requestPasswordThroughEmail(PasswordRequestThroughEmail request) throws CustomException {
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
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
            throw new CustomException("Token not found!", HttpStatus.BAD_REQUEST);
        }
        AccountDetailsCustom accountDetailsCustom = (AccountDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account currentUser = accountRepository.findById(accountDetailsCustom.getId()).orElseThrow(() -> new CustomException("Invalid token!", HttpStatus.NOT_FOUND));
        String currentPassword = currentUser.getPassword();
        if (!passwordEncoder.matches(request.getCurrentPassword(), currentPassword)) {
            throw new CustomException("Incorrect current password!", HttpStatus.BAD_REQUEST);
        } else {
            currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
            accountRepository.save(currentUser);
        }
    }

    @Override
    public boolean accountVerify(String email, Integer otp) throws CustomException {
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new CustomException("Account not found", HttpStatus.NOT_FOUND));
        Company company = companyRepository.findByAccountId(account.getId()).orElse(null);
        if (otp.equals(account.getOtp())) {
            if (company != null) {
                account.setStatus(3);
                account.setOtp(null);
                return true;
            } else {
                account.setStatus(1);
                account.setOtp(null);
            }
        } else {
            throw new CustomException("Invalid OTP", HttpStatus.BAD_REQUEST);
        }
        return true;
    }

    @Override
    public boolean resendOtp(String email) throws CustomException {
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new CustomException("Account not found", HttpStatus.NOT_FOUND));
        if (account.getStatus() == 1) {
            throw new CustomException("Your account has been verify", HttpStatus.BAD_REQUEST);
        }
        Integer otp = otpGenerator();
        emailService.sendSimpleMessage(new MailBody(account.getEmail(), "Verify account", "Your otp is: " + otp));
        account.setOtp(otp);
        accountRepository.save(account);
        return true;
    }

    @Override
    public ResponseEntity<Integer> changeStatusAcount(Integer companyId) {
        Optional<Company> company = companyRepository.findById(companyId);
        if (company.isPresent()) {
            Company company1 = company.get();
            Integer s = company1.getAccount().getId();
            Optional<Account> accountOptional = accountRepository.findById(s);

            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();

                if (account.getStatus() == 3) {
                    account.setStatus(1);  // Chuyển từ 3 về 1
                    accountRepository.save(account);  // Lưu thay đổi
                }

                return ResponseEntity.ok(account.getStatus());
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return null;
    }




//    }
//        Optional<Account> account = accountRepository.findById();
//        if (account.isPresent()) {
//           Account account1 =  account.get();
//           if (account1.getStatus() == 3){
//               account1.setStatus(1);
//           }
//
//        }
//        return null;
//    }
}
