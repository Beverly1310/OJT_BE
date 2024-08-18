package com.example.ojt.service.company;

import com.example.ojt.exception.CustomException;
import com.example.ojt.exception.IdFormatException;
import com.example.ojt.model.dto.request.EditCompanyRequest;
import com.example.ojt.model.dto.response.CandidateInfoRes;
import com.example.ojt.model.dto.response.CompanyResponse;
import com.example.ojt.model.entity.*;
import com.example.ojt.repository.*;
import com.example.ojt.model.entity.AddressCompany;
import com.example.ojt.model.entity.Company;
import com.example.ojt.model.entity.Location;
import com.example.ojt.model.entity.TypeCompany;
import com.example.ojt.repository.IAddressCompanyRepository;
import com.example.ojt.repository.ICompanyRepository;
import com.example.ojt.repository.ILocationRepository;
import com.example.ojt.repository.ITypeCompanyRepository;
import com.example.ojt.security.principle.AccountDetailsCustom;
import com.example.ojt.service.UploadService;
import com.example.ojt.service.account.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CompanyService implements ICompanyService {
    private final ICompanyRepository companyRepository;
    private final UploadService uploadService;
    private final ITypeCompanyRepository typeCompanyRepository;
    private final IAddressCompanyRepository addressCompanyRepository;
    private final ILocationRepository locationRepository;
    private final ICandidateRepository candidateRepository;
    private final ISkillCandidateRepository skillCandidateRepository;
    private final IExperienceCandidateRepository experienceCandidateRepository;
    private final IEducationCandidateRepository educationCandidateRepository;
    private final IProjectCandidateRepository projectCandidateRepository;
    private final ICertificateRepository certificateRepository;

    private  Company getCurrentCompany() throws CustomException {
        Company company = companyRepository.findByAccountId(AccountService.getCurrentUser().getId()).orElseThrow(() -> new CustomException("Company not found" , HttpStatus.NOT_FOUND));
        return company;
    }
    @Override
    public CompanyResponse findCurrentCompany() throws CustomException {
        Company company = getCurrentCompany();
        return convertToCompanyResponse(company);
    }
    @Override
    public Page<CompanyResponse> findAllCompanies(Pageable pageable, String locationName, String companyName) {
        Page<Company> companies;
        if ((companyName == null || companyName.isEmpty()) && (locationName == null || locationName.isEmpty())) {
            companies = companyRepository.findAll(pageable);
        } else {
            companies = companyRepository.findAllByCompanyNameAndLocation(
                    companyName != null ? companyName : "",
                    locationName != null ? locationName : "",
                    pageable
            );
        }

        return companies.map(this::convertToCompanyResponse);
    }

    private CompanyResponse convertToCompanyResponse(Company company) {
        // Giả sử mỗi công ty có ít nhất một địa chỉ, lấy address, mapUrl và nameCity từ địa chỉ đầu tiên.
        Optional<AddressCompany> firstAddress = company.getAddressCompanySet().stream().findFirst();

        String nameCity = firstAddress.map(addressCompany -> addressCompany.getLocation().getNameCity()).orElse("N/A");
        String address = firstAddress.map(AddressCompany::getAddress).orElse("N/A");
        String mapUrl = firstAddress.map(AddressCompany::getMapUrl).orElse("N/A");

        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .logo(company.getLogo())
                .website(company.getWebsite())
                .linkFacebook(company.getLinkFacebook())
                .linkLinkedin(company.getLinkLinkedin())
                .followers(company.getFollowers())
                .size(company.getSize())
                .description(company.getDescription())
                .phone(company.getPhone())
                .emailCompany(company.getEmailCompany())
                .policy(company.getPolicy())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .typeCompanyName(company.getTypeCompany() != null ? company.getTypeCompany().getName() : "N/A")
                .nameCity(nameCity)
                .address(address)  // Chỉ cần lấy address một lần
                .mapUrl(mapUrl)    // Chỉ cần lấy mapUrl một lần
                .build();
    }


    @Override
    public CompanyResponse findById(Integer id) throws CustomException {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CustomException("Company not found with id " + id, HttpStatus.NOT_FOUND));
        return convertToCompanyResponse(company);
    }


    @Override
    public List<CompanyResponse> findCompaniesByTypeCompany(Integer companyId) {
        Optional<Company> companyDetail = companyRepository.findById(companyId);
        if (companyDetail.isPresent()){
            Company company = companyDetail.get();
            TypeCompany typeCompany = company.getTypeCompany();
            if (typeCompany != null){
                List<Company> companies = companyRepository.findByTypeCompany(typeCompany);

                // Lọc danh sách các công ty để loại trừ công ty hiện tại
                List<Company> filteredCompanies = companies.stream()
                        .filter(c -> !c.getId().equals(companyId))
                        .toList();

                return filteredCompanies.stream().map(this::convertToCompanyResponse).toList();
            }
        }
        return Collections.emptyList();
    }



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

                        Set<AddressCompany> companyAddressSet = company.getAddressCompanySet();
                        if (!companyAddressSet.isEmpty()) {
                            AddressCompany companyAddress = companyAddressSet.iterator().next(); // Renamed the variable to avoid conflict
                            companyAddress.setLocation(location);

                            if (companyRequest.getMapUrl() != null) {
                                companyAddress.setMapUrl(companyRequest.getMapUrl());
                            }

                            addressCompanyRepository.save(companyAddress); // Save the updated AddressCompany
                        }
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


    @Override
    public ResponseEntity<Integer> changeOutstandingStatus(Integer companyId) {
        Optional<Company> company = companyRepository.findById(companyId);
        if (company.isPresent()) {
            Company company1 = company.get();
            company1.setOutstanding(company1.getOutstanding() == 1 ? 0 : 1);
            companyRepository.save(company1);
            return ResponseEntity.status(HttpStatus.OK).body(company1.getOutstanding());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public Long countCompanies() {
        return companyRepository.count();
    }

    // @Override
    //    public ResponseEntity<Page<UserResponsedto>> findAllUser(Pageable pageable) {
    //        Page<User> user = userRepository.findAll(pageable);
    //        Page<UserResponsedto> response = user.map(this::    mapToUserResponse);
    //        return ResponseEntity.status(HttpStatus.OK).body(response);
    //    }

    @Override
    public CandidateInfoRes getCandidateInfoById(Integer candidateId) throws CustomException {
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(()-> new CustomException("Candidate not found", HttpStatus.NOT_FOUND));
        CandidateInfoRes candidateInfoRes = CandidateInfoRes.builder()
                .id(candidateId)
                .name(candidate.getName())
                .birthday(candidate.getBirthday())
                .address(candidate.getAddress())
                .phone(candidate.getPhone())
                .linkLinkedin(candidate.getLinkLinkedin())
                .linkGit(candidate.getLinkGit())
                .position(candidate.getPosition())
                .avatar(candidate.getAvatar())
                .aboutme(candidate.getAboutme())
                .email(candidate.getAccount().getEmail())
                .skills(skillCandidateRepository.findAllByCandidateId(candidateId).stream().map(SkillsCandidate::getName).toList())
                .exp(experienceCandidateRepository.findAllByCandidateId(candidateId).stream().map(ExperienceCandidate::getCompany).toList())
                .edu(educationCandidateRepository.findAllByCandidateId(candidateId).stream().map(EducationCandidate::getNameEducation).toList())
                .project(projectCandidateRepository.findAllByCandidateId(candidateId).stream().map(ProjectCandidate::getName).toList())
                .certi(certificateRepository.findAllByCandidateId(candidateId).stream().map(CertificateCandidate::getName).toList())
                .build();
        return candidateInfoRes;
    }
}
