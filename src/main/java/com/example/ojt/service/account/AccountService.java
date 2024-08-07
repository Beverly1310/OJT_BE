package com.example.ojt.service.account;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.LoginAccountRequest;
import com.example.ojt.model.dto.request.RegisterAccountCandidate;
import com.example.ojt.model.dto.request.UpdateAccountCandidate;
import com.example.ojt.model.dto.response.JWTResponse;
import com.example.ojt.model.entity.Account;
import com.example.ojt.model.entity.Candidate;
import com.example.ojt.model.entity.RoleName;
import com.example.ojt.repository.IAccountRepository;
import com.example.ojt.repository.ICandidateRepository;
import com.example.ojt.repository.IRoleRepository;
import com.example.ojt.security.jwt.JWTProvider;
import com.example.ojt.security.principle.AccountDetailsCustom;
import com.example.ojt.service.UploadService;
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
import java.util.Objects;

@Service
@Transactional
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
    private ICandidateRepository candidateRepository;
    @Autowired
    private UploadService uploadService;

    public static AccountDetailsCustom getCurrentUser() {
        return (AccountDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
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
    @Transactional
    public boolean registerCandidate(RegisterAccountCandidate registerAccountCandidate) throws CustomException {
        if (accountRepository.existsByEmail(registerAccountCandidate.getEmail())) {
            throw new CustomException("Email existed!", HttpStatus.CONFLICT);
        }
        if (!registerAccountCandidate.getPassword().equals(registerAccountCandidate.getConfirmPassword())) {
            throw new CustomException("Password do not match!", HttpStatus.BAD_REQUEST);
        }
        Account account = Account.builder()
                .name(registerAccountCandidate.getName())
                .email(registerAccountCandidate.getEmail())
                .password(passwordEncoder.encode(registerAccountCandidate.getPassword()))
                .status(1)
                .role(roleRepository.findByRoleName(RoleName.valueOf("ROLE_CANDIDATE")).orElseThrow(() -> new CustomException("Role not found", HttpStatus.NOT_FOUND)))
                .build();
        Candidate candidate = Candidate.builder()
                .name(account.getName())
                .account(account)
                .status(1)
                .createdAt(new Date())
                .avatar("https://png.pngtree.com/png-vector/20220608/ourmid/pngtree-man-avatar-isolated-on-white-background-png-image_4891418.png")
                .build();
        accountRepository.save(account);
        candidateRepository.save(candidate);
        return true;
    }

    @Override
    public boolean updateCandidate(UpdateAccountCandidate updateAccountCandidate) throws CustomException {
        Candidate candidate = candidateRepository.findById(getCurrentUser().getId()).orElseThrow(() -> new CustomException("Candidate not found", HttpStatus.NOT_FOUND));
//        Candidate candidate = candidateRepository.findById(1).orElseThrow(() -> new CustomException("Candidate not found", HttpStatus.NOT_FOUND));
          if(updateAccountCandidate.getAboutMe()!=null && !updateAccountCandidate.getAboutMe().isBlank()){
              candidate.setAboutme(updateAccountCandidate.getAboutMe());
          }
          if(updateAccountCandidate.getAddress()!=null && !updateAccountCandidate.getAddress().isBlank()){
              candidate.setAddress(updateAccountCandidate.getAddress());
          }
          if (updateAccountCandidate.getAvatar()!=null && !updateAccountCandidate.getAvatar().isEmpty()){
              candidate.setAvatar(uploadService.uploadFileToServer(updateAccountCandidate.getAvatar()));
          }
          if (updateAccountCandidate.getBirthDay()!=null){
              candidate.setBirthday(updateAccountCandidate.getBirthDay());
          }
          if (updateAccountCandidate.getGender()!=null){
              candidate.setGender(updateAccountCandidate.getGender());
          }
          if (updateAccountCandidate.getLinkGit()!=null&&!updateAccountCandidate.getLinkGit().isBlank()){
              candidate.setLinkGit(updateAccountCandidate.getLinkGit());
          }
          if ((updateAccountCandidate.getLinkLinkedin()!=null&&!updateAccountCandidate.getLinkLinkedin().isBlank())){
              candidate.setLinkLinkedin(updateAccountCandidate.getLinkLinkedin());
          }
          if (updateAccountCandidate.getName()!=null && !updateAccountCandidate.getName().isBlank()){
              candidate.setName(updateAccountCandidate.getName());
          }
          if (updateAccountCandidate.getPhone()!=null && !updateAccountCandidate.getPhone().isBlank()){
              candidate.setPhone(updateAccountCandidate.getPhone());
          }
          if (updateAccountCandidate.getPosition()!=null && !updateAccountCandidate.getPosition().isBlank()){
              candidate.setPosition(updateAccountCandidate.getPosition());
          }
          candidate.setUpdatedAt(new Date());
          candidateRepository.save(candidate);
        return true;
    }
}
