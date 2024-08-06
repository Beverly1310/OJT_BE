package com.example.ojt.service.account;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.LoginAccountRequest;
import com.example.ojt.model.dto.request.RegisterAccount;
import com.example.ojt.model.dto.request.RegisterAccountCompanyRequest;
import com.example.ojt.model.dto.response.JWTResponse;
import com.example.ojt.model.dto.response.MailBody;
import com.example.ojt.model.entity.*;
import com.example.ojt.repository.*;
import com.example.ojt.security.jwt.JWTProvider;
import com.example.ojt.security.principle.AccountDetailsCustom;

import com.example.ojt.service.company.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;

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
    @Override
    public JWTResponse login(LoginAccountRequest loginAccountRequest) throws CustomException {
        // Xac thuc email and password
        Authentication authentication = null;
        try {
            authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(loginAccountRequest.getEmail(), loginAccountRequest.getPassword()));
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

        Company company = Company.builder()
                .name(account.getName())
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
        accountRepository.save(account);
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
}
