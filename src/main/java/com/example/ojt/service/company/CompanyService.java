package com.example.ojt.service.company;

import com.example.ojt.exception.CustomException;
import com.example.ojt.exception.IdFormatException;
import com.example.ojt.model.dto.request.EditCompanyRequest;
import com.example.ojt.model.entity.Company;
import com.example.ojt.model.entity.TypeCompany;
import com.example.ojt.repository.ICompanyRepository;
import com.example.ojt.repository.ITypeCompanyRepository;
import com.example.ojt.security.principle.AccountDetailsCustom;
import com.example.ojt.service.UploadService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService implements ICompanyService {
    private final ICompanyRepository companyRepository;
    private final UploadService uploadService;
    private final ITypeCompanyRepository typeCompanyRepository;


    @Override
    public boolean update(EditCompanyRequest companyRequest) throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDetailsCustom accountDetails = (AccountDetailsCustom) authentication.getPrincipal();
        Integer userId = accountDetails.getId();

        Company company = companyRepository.findById(userId)
                .orElseThrow(() -> new CustomException("Company is not found with this id " + userId, HttpStatus.NOT_FOUND));

        if (company != null) {
            // Update fields
            if (companyRequest.getName() != null) {
                company.setName(companyRequest.getName());
            }
            if (companyRequest.getLogo() != null) {
                company.setLogo(uploadService.uploadFileToServer(companyRequest.getLogo()));
            }
            if (companyRequest.getWebsite() != null) {
                company.setWebsite(companyRequest.getWebsite());
            }
            if (companyRequest.getLinkFacebook() != null) {
                company.setLinkFacebook(companyRequest.getLinkFacebook());
            }
            if (companyRequest.getLinkLinkedin() != null) {
                company.setLinkLinkedin(companyRequest.getLinkLinkedin());
            }
            if (companyRequest.getSize() != null) {
                if (companyRequest.getSize() > 0) {
                    company.setSize(companyRequest.getSize());
                } else {
                    throw new CustomException("Size must be greater than or equal to 0", HttpStatus.BAD_REQUEST);
                }
            }
            if (companyRequest.getDescription() != null) {
                company.setDescription(companyRequest.getDescription());
            }
            if (companyRequest.getPhone() != null) {
                company.setPhone(companyRequest.getPhone());
            }
            if (companyRequest.getPolicy() != null) {
                company.setPolicy(companyRequest.getPolicy());
            }
            if (companyRequest.getTypeCompany() != null) {
                TypeCompany typeCompany = typeCompanyRepository.findById(companyRequest.getTypeCompany())
                        .orElseThrow(() -> new CustomException("TypeCompany not found with id " + companyRequest.getTypeCompany(), HttpStatus.NOT_FOUND));
                company.setTypeCompany(typeCompany);
            }

            company.setUpdatedAt(new Date());

            // Save updated company
            companyRepository.save(company);
            return true;
        }
        return false;
    }

    @Override
    public ResponseEntity<Page<Company>> getAllCompanies(Pageable pageable) {
        Page<Company> companies = companyRepository.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(companies);
    }


    @Override
    @Transactional
    public void deleteCompany(Integer id) throws IdFormatException {
        if (id == null || id < 0) {
            throw new IdFormatException("Invalid ID format");
        }
        if (!companyRepository.existsById(id)) {
            throw new IdFormatException("Company with ID " + id + " does not exist");
        }
        companyRepository.deleteById(id);
    }


    // @Override
    //    public ResponseEntity<Page<UserResponsedto>> findAllUser(Pageable pageable) {
    //        Page<User> user = userRepository.findAll(pageable);
    //        Page<UserResponsedto> response = user.map(this::    mapToUserResponse);
    //        return ResponseEntity.status(HttpStatus.OK).body(response);
    //    }
}
