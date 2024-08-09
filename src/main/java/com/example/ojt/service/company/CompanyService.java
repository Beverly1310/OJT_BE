package com.example.ojt.service.company;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.EditCompanyRequest;
import com.example.ojt.model.entity.AddressCompany;
import com.example.ojt.model.entity.Company;
import com.example.ojt.model.entity.Location;
import com.example.ojt.model.entity.TypeCompany;
import com.example.ojt.repository.ICompanyRepository;
import com.example.ojt.repository.ILocationRepository;
import com.example.ojt.repository.ITypeCompanyRepository;
import com.example.ojt.security.principle.AccountDetailsCustom;
import com.example.ojt.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompanyService implements ICompanyService {
    private final ICompanyRepository companyRepository;
    private final UploadService uploadService;
    private final ITypeCompanyRepository typeCompanyRepository;
    private final ILocationRepository locationRepository;


    @Override
    public boolean update(EditCompanyRequest companyRequest) throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDetailsCustom accountDetails = (AccountDetailsCustom) authentication.getPrincipal();
        Integer userId = accountDetails.getId();

        Company company = companyRepository.findByAccountId(userId)
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
            if (companyRequest.getAddress() != null || companyRequest.getMapUrl() != null || companyRequest.getLocationId() != null) {
                Set<AddressCompany> addressCompanySet = company.getAddressCompanySet();
                if (!addressCompanySet.isEmpty()) {
                    AddressCompany addressCompany = addressCompanySet.iterator().next();

                    if (companyRequest.getAddress() != null) {
                        addressCompany.setAddress(companyRequest.getAddress());
                    }
                    if (companyRequest.getMapUrl() != null) {
                        addressCompany.setMapUrl(companyRequest.getMapUrl());
                    }
                    if (companyRequest.getLocationId() != null) {
                        Location location = locationRepository.findById(companyRequest.getLocationId())
                                .orElseThrow(() -> new CustomException("Location not found with id " + companyRequest.getLocationId(), HttpStatus.NOT_FOUND));
                        addressCompany.setLocation(location);
                    }

                    addressCompany.setCreatedAt(new Date()); // Update timestamp
                }
            }
            company.setUpdatedAt(new Date());

            // Save updated company
            companyRepository.save(company);
            return true;
        }
        return false;
    }
}
