package com.example.ojt.service.job;


import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.JobAddRequest;
import com.example.ojt.model.dto.request.JobRequest;
import com.example.ojt.model.dto.response.*;
import com.example.ojt.model.entity.*;
import com.example.ojt.repository.*;
import com.example.ojt.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import java.util.Optional;

import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
public class JobService implements IJobService{
    @Autowired
    private IJobRepository jobRepository;
    @Autowired
    private ICompanyRepository companyRepository;

    @Autowired
    private IAddressCompanyRepository addressCompanyRepository;

    @Autowired
    private ILevelJobRepository levelJobRepository;

    @Autowired
    private ILevelsJobsRepository levelsJobsRepository;

    @Autowired
    private ITypeJobRepository typeJobRepository;

    @Autowired
    private ITypesJobsRepository typesJobsRepository;

    @Autowired
    private ILocationRepository locationRepository;

    @Autowired
    private ICandidateRepository candidateRepository;

    private  Company getCurrentCompany() throws CustomException {
        Company company = companyRepository.findByAccountId(AccountService.getCurrentUser().getId()).orElseThrow(() -> new CustomException("Company not found" , HttpStatus.NOT_FOUND));
        return company;
    }

    @Override
    public Page<JobResponse> findAll(Pageable pageable, String search, String location) {
        Page<Job> jobs;
        if (search.isEmpty() && location.isEmpty()) {
            jobs = jobRepository.findAll(pageable);
        } else if (location.isEmpty()) {
            jobs = jobRepository.findAllByTitleContains(search, pageable);
        } else {
            jobs = jobRepository.findAllByTitleContainsAndAddressCompany_Location_NameCityContains(search, location, pageable);
        }
        return jobs.map(this::convertToJobResponse);
    }


    // Phương thức mới để lấy danh sách các Job theo Company
    @Override
    @Transactional(readOnly = true)
    public Page<JobResponse> findAllByCurrentCompany(String title, String location, Pageable pageable) throws CustomException {
        Company company = getCurrentCompany();
        Page<Job> jobs = jobRepository.findAllByCompanyAndTitleContainingAndLocationContaining(company, title, location, pageable);
        return jobs.map(this::convertToJobResponse);
    }

    private JobResponse convertToJobResponse(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .requirements(job.getRequirements())
                .salary(job.getSalary())
                .expireAt(job.getExpireAt())
                .createdAt(job.getCreatedAt())
                .status(job.getStatus())
                .companyName(job.getCompany().getName())
                .address(job.getAddressCompany().getAddress())
                .city(job.getAddressCompany().getLocation().getNameCity())
                .companyLogo(job.getCompany().getLogo())
                .typeJob(typeJobRepository.findByJobId(job.getId()))
                .levelJob(levelJobRepository.findLevelJobNamesByJobId(job.getId()))
                .build();
    }


    @Override
    @Transactional
    public boolean addJob(JobAddRequest jobRequest) throws CustomException {
        Company company = getCurrentCompany();
        Integer locationId =    addressCompanyRepository.findByCompanyId(getCurrentCompany().getId()).orElseThrow(()->new CustomException("Location not found", HttpStatus.NOT_FOUND)).getLocation().getId();
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new CustomException("Location not found", HttpStatus.NOT_FOUND));


        List<AddressCompany> addressCompanies = addressCompanyRepository.findByLocation(location);

        // Kiểm tra nếu danh sách rỗng
        if (addressCompanies.isEmpty()) {
            throw new CustomException("Address Company not found", HttpStatus.NOT_FOUND);
        }

        // Nếu có nhiều kết quả, bạn có thể lấy kết quả đầu tiên hoặc thêm điều kiện lọc
        AddressCompany addressCompany = addressCompanies.stream()
                .filter(ac -> ac.getCompany().getId().equals(company.getId()))
                .findFirst()
                .orElseThrow(() -> new CustomException("Suitable Address Company not found", HttpStatus.NOT_FOUND));

        if (jobRepository.findByTitle(jobRequest.getTitle()).orElse(null) != null) {
            throw new CustomException("Job already exists", HttpStatus.BAD_REQUEST);
        }

        Job job = Job.builder()
                .title(jobRequest.getTitle())
                .description(jobRequest.getDescription())
                .requirements(jobRequest.getRequirements())
                .salary(jobRequest.getSalary())
                .expireAt(jobRequest.getExpireAt())
                .createdAt(new Timestamp(new Date().getTime()))
                .status(1)
                .company(company)
                .addressCompany(addressCompany)
                .build();

        jobRepository.save(job);

        // Link with LevelJobs
        List<LevelJob> levelJobs = levelJobRepository.findAllById(jobRequest.getLevelJobIds());
        for (LevelJob levelJob : levelJobs) {
            LevelsJobs levelsJobs = LevelsJobs.builder()
                    .job(job)
                    .levelJob(levelJob)
                    .build();
            levelsJobsRepository.save(levelsJobs);
        }

        // Link with TypeJobs
        List<TypeJob> typeJobs = typeJobRepository.findAllById(jobRequest.getTypeJobIds());
        for (TypeJob typeJob : typeJobs) {
            TypesJobs typesJobs = TypesJobs.builder()
                    .job(job)
                    .typeJob(typeJob)
                    .build();
            typesJobsRepository.save(typesJobs);
        }

        return true;
    }



    @Override
    @Transactional
    public boolean updateJob(JobRequest jobRequest) throws CustomException {
        try {
            Company currentCompany = getCurrentCompany();

            Job job = jobRepository.findById(jobRequest.getId())
                    .orElseThrow(() -> new CustomException("Job not found", HttpStatus.NOT_FOUND));

            if (!job.getCompany().getId().equals(currentCompany.getId())) {
                throw new CustomException("You do not have permission to update this job", HttpStatus.FORBIDDEN);
            }

            Job jobCheck = jobRepository.findByTitle(jobRequest.getTitle()).orElse(null);
            if (jobCheck != null && !jobCheck.getId().equals(job.getId())) {
                throw new CustomException("Job already exists", HttpStatus.BAD_REQUEST);
            }

            if (jobRequest.getTitle() != null && !jobRequest.getTitle().isEmpty()) {
                job.setTitle(jobRequest.getTitle());
            }
            if (jobRequest.getDescription() != null && !jobRequest.getDescription().isEmpty()) {
                job.setDescription(jobRequest.getDescription());
            }
            if (jobRequest.getRequirements() != null && !jobRequest.getRequirements().isEmpty()) {
                job.setRequirements(jobRequest.getRequirements());
            }
            if (jobRequest.getSalary() != null && !jobRequest.getSalary().isEmpty()) {
                job.setSalary(jobRequest.getSalary());
            }
            if (jobRequest.getExpireAt() != null) {
                job.setExpireAt(jobRequest.getExpireAt());
            }

            if (jobRequest.getLevelJobIds() != null && !jobRequest.getLevelJobIds().isEmpty()) {
                levelsJobsRepository.deleteAllByJobId(job.getId());
                List<LevelJob> levelJobs = levelJobRepository.findAllById(jobRequest.getLevelJobIds());
                for (LevelJob levelJob : levelJobs) {
                    LevelsJobs levelsJobs = LevelsJobs.builder()
                            .job(job)
                            .levelJob(levelJob)
                            .build();
                    levelsJobsRepository.save(levelsJobs);
                }
            }

            if (jobRequest.getTypeJobIds() != null && !jobRequest.getTypeJobIds().isEmpty()) {
                typesJobsRepository.deleteAllByJobId(job.getId());
                List<TypeJob> typeJobs = typeJobRepository.findAllById(jobRequest.getTypeJobIds());
                for (TypeJob typeJob : typeJobs) {
                    TypesJobs typesJobs = TypesJobs.builder()
                            .job(job)
                            .typeJob(typeJob)
                            .build();
                    typesJobsRepository.save(typesJobs);
                }
            }

            jobRepository.save(job);

            return true;
        } catch (Exception e) {
            throw new CustomException("Failed to update job", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @Override
    public boolean deleteJob(Integer deleteId) throws CustomException {
        Job job = jobRepository.findByIdAndCompany(deleteId,getCurrentCompany()).orElseThrow(()-> new CustomException("Job not found", HttpStatus.NOT_FOUND));
        job.setStatus(2);
        jobRepository.save(job);
        return true;
    }

    @Override
    public SuccessResponse findById(Integer findId) throws CustomException {
        try {
            // Fetch the Job entity
            Job job = jobRepository.findById(findId)
                    .orElseThrow(() -> new CustomException("Job with ID " + findId + " not found", HttpStatus.NOT_FOUND));

            // Fetch LevelsJobs associated with this job
            Set<LevelsJobs> levelsJobs = levelsJobsRepository.findByJobId(findId);

            // Map LevelsJobs to LevelsJobsDTO
            Set<LevelsJobsDTO> levelsJobsDTOs = levelsJobs.stream()
                    .map(levelsJob -> LevelsJobsDTO.builder()
                            .id(levelsJob.getId())
                            .levelJob(LevelJobDTO.builder()
                                    .id(levelsJob.getLevelJob().getId())
                                    .name(levelsJob.getLevelJob().getName())
                                    .build())
                            .build())
                    .collect(Collectors.toSet());

            // Extract LevelJobDTOs
            Set<LevelJobDTO> levelJobDTOs = levelsJobsDTOs.stream()
                    .map(LevelsJobsDTO::getLevelJob)
                    .collect(Collectors.toSet());

            // Map CompanyDTO and AddressCompanyDTO
            CompanyDTO companyDTO = CompanyDTO.builder()
                    .id(job.getCompany().getId())
                    .name(job.getCompany().getName())
                    .logo(job.getCompany().getLogo())
                    .website(job.getCompany().getWebsite())
                    .linkFacebook(job.getCompany().getLinkFacebook())
                    .linkLinkedin(job.getCompany().getLinkLinkedin())
                    .followers(job.getCompany().getFollowers())
                    .size(job.getCompany().getSize())
                    .outstanding(Optional.ofNullable(job.getCompany().getOutstanding()).orElse(0)) // Handle null value
                    .description(job.getCompany().getDescription())
                    .phone(job.getCompany().getPhone())
                    .emailCompany(job.getCompany().getEmailCompany())
                    .policy(job.getCompany().getPolicy())
                    .build();

            AddressCompany addressCompany = job.getAddressCompany();
            LocationDTO locationDTO = LocationDTO.builder()
                    .id(addressCompany.getLocation().getId())
                    .nameCity(addressCompany.getLocation().getNameCity())
                    .build();

            AddressCompanyDTO addressCompanyDTO = AddressCompanyDTO.builder()
                    .id(addressCompany.getId())
                    .address(addressCompany.getAddress())
                    .mapUrl(addressCompany.getMapUrl())
                    .createdAt(addressCompany.getCreatedAt())
                    .status(addressCompany.getStatus())
                    .location(locationDTO)
                    .build();

            // Create JobDetailDTO
            JobDetailDTO jobDetailDTO = JobDetailDTO.builder()
                    .id(job.getId())
                    .title(job.getTitle())
                    .description(job.getDescription())
                    .requirements(job.getRequirements())
                    .salary(job.getSalary())
                    .expireAt(job.getExpireAt())
                    .createdAt(job.getCreatedAt())
                    .outstanding(Optional.ofNullable(job.getOutstanding()).orElse(0)) // Handle null value
                    .status(job.getStatus())
                    .levelJobs(levelJobDTOs) // Use the extracted Set<LevelJobDTO>
                    .company(companyDTO)
                    .addressCompany(addressCompanyDTO)
                    .build();

            return SuccessResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Get job success")
                    .data(jobDetailDTO)
                    .build();
        } catch (CustomException e) {
            throw e; // Rethrow to be caught by controller
        } catch (Exception e) {
            throw new CustomException("Unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @Override
    public ResponseEntity<?> getAllJobs(Pageable pageable) {
       Page<Job>  jobs =  jobRepository.findAll(pageable);
         return ResponseEntity.status(HttpStatus.OK).body(jobs);
    }

    @Override
    public ResponseEntity<Integer> changeOutstandingStatus(Integer jobId) {
        Optional<Job> job = jobRepository.findById(jobId);
        if (job.isPresent()) {
            Job job1 = job.get();
            job1.setOutstanding(job1.getOutstanding() == 1 ? 0 : 1);
            jobRepository.save(job1);
            return ResponseEntity.status(HttpStatus.OK).body((int) job1.getOutstanding());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @Override
    public List<Job> getJobsBySameType(Integer jobId) {
        // Tìm tất cả các loại công việc liên quan đến công việc
        Set<String> typeNames = typesJobsRepository.findTypeNamesByJobId(jobId);

        // Tìm tất cả các công việc có cùng loại
        return jobRepository.findByTypesJobs_NameIn(typeNames);

    }


    @Override
    @Transactional(readOnly = true)
    public Page<JobResponse> findAllByCompanyAndSearch(Integer companyId, String title, String location, Pageable pageable) throws CustomException {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CustomException("Company not found with id: " + companyId,HttpStatus.NOT_FOUND));


        Page<Job> jobs = jobRepository.findAllByCompanyAndTitleContainingAndLocationContaining(company, title, location, pageable);


        return jobs.map(this::convertToJobResponse);
    }




}
