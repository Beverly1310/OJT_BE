package com.example.ojt.service.job;


import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.JobAddRequest;
import com.example.ojt.model.dto.request.JobRequest;
import com.example.ojt.model.dto.response.JobResponse;
import com.example.ojt.model.dto.response.SuccessResponse;
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
        Company company = getCurrentCompany(); // Phương thức lấy thông tin công ty hiện tại
        Page<Job> jobs = jobRepository.findAllByCompanyAndTitleContainingAndLocationContaining(company, title, location, pageable);
        return jobs.map(this::convertToJobResponse); // Chuyển đổi Job entity sang JobResponse
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
                .build();
    }

    @Override
    @Transactional
    public boolean addJob(JobAddRequest jobRequest) throws CustomException {
        Company company = getCurrentCompany(); // Lấy company từ tài khoản hiện tại

        AddressCompany addressCompany = addressCompanyRepository.findById(jobRequest.getAddressCompanyId())
                .orElseThrow(() -> new CustomException("Address Company not found", HttpStatus.NOT_FOUND));
        if (jobRepository.findByTitle(jobRequest.getTitle()).orElse(null) != null) {
            throw new CustomException("Job already exist", HttpStatus.BAD_REQUEST);
        }
        Job job = Job.builder()
                .title(jobRequest.getTitle())
                .description(jobRequest.getDescription())
                .requirements(jobRequest.getRequirements())
                .salary(jobRequest.getSalary())
                .expireAt(jobRequest.getExpireAt())
                .createdAt(new Timestamp(new Date().getTime()))
                .status(1)
                .company(company) // Liên kết với company hiện tại
                .addressCompany(addressCompany)
                .build();
        jobRepository.save(job);

        // Liên kết với các LevelJob
        List<LevelJob> levelJobs = levelJobRepository.findAllById(jobRequest.getLevelJobIds());
        for (LevelJob levelJob : levelJobs) {
            LevelsJobs levelsJobs = LevelsJobs.builder()
                    .job(job)
                    .levelJob(levelJob)
                    .build();
            levelsJobsRepository.save(levelsJobs);
        }



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
            Job job = jobRepository.findById(jobRequest.getId())
                    .orElseThrow(() -> new CustomException("Job not found", HttpStatus.NOT_FOUND));
            Job jobCheck = jobRepository.findByTitle(job.getTitle()).orElse(null);
            if (jobCheck!=null && jobCheck.getId()!=job.getId()){
                throw new CustomException("Job already exist", HttpStatus.BAD_REQUEST);
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
            if (jobRequest.getCompanyId() != null) {
                Company company = companyRepository.findById(jobRequest.getCompanyId())
                        .orElseThrow(() -> new CustomException("Company not found", HttpStatus.NOT_FOUND));
                job.setCompany(company);
            }
            if (jobRequest.getAddressCompanyId() != null) {
                AddressCompany addressCompany = addressCompanyRepository.findById(jobRequest.getAddressCompanyId())
                        .orElseThrow(() -> new CustomException("Address Company not found", HttpStatus.NOT_FOUND));
                job.setAddressCompany(addressCompany);
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
            Job job = jobRepository.findById(findId).orElseThrow(() -> new CustomException("Job not found" , HttpStatus.NOT_FOUND));
            return SuccessResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("get job success")
                    .data(job)
                    .build();
        }catch (Exception e){
            throw new CustomException("Error finding Job" , HttpStatus.INTERNAL_SERVER_ERROR);
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
}
