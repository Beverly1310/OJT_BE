package com.example.ojt.repository;

import com.example.ojt.model.dto.response.JobResponse;
import com.example.ojt.model.entity.Company;
import com.example.ojt.model.entity.Job;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface IJobRepository extends JpaRepository<Job,Integer> {
    Page<Job> findAllByTitleContainsAndAddressCompany_Location_NameCityContains(String title, String nameCity, Pageable pageable);
Page<Job> findAllByTitleContains(String title , Pageable pageable);
Optional<Job> findByTitle(String title);
Optional<Job> findByIdAndCompany(Integer id, Company company);
    Optional<Job> findFirstByCompany(Company company);
//    @Override
//    @Query("SELECT j FROM Job j WHERE j.status = 1")
//    List<Job> findAll();  // Override phương thức findAll với custom query

    @Override
    @Query("SELECT j FROM Job j WHERE j.status = 1")
    <S extends Job> List<S> findAll(Example<S> example);

    @Override
    @Query("SELECT j FROM Job j WHERE j.status = 1")
    Page<Job> findAll(Pageable pageable);

    @Query("SELECT j FROM Job j JOIN TypesJobs tj ON j.id = tj.job.id WHERE tj.typeJob.name IN :typeNames")
    List<Job> findByTypesJobs_NameIn(@Param("typeNames") Set<String> typeNames);

    @Query("SELECT j FROM Job j " +
            "WHERE j.company = :company " +
            "AND (:title IS NULL OR j.title LIKE %:title%) " +
            "AND (:location IS NULL OR j.addressCompany.location.nameCity LIKE %:location%) "
    + "AND j.status = 1")
    Page<Job> findAllByCompanyAndTitleContainingAndLocationContaining(
            @Param("company") Company company,
            @Param("title") String title,
            @Param("location") String location,
            Pageable pageable);

    @Query("select j from Job j where j.outstanding=:outstanding and j.status=1")
    List<Job> getJobByOutstanding(Integer outstanding);
    @Query("select count(j) from Job j where j.status=1")
    Integer getCountJob();

    @Query("select count(j) from Job j where j.createdAt between :startDate and :endDate and j.status=1")
    Integer getCountNewJob(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

//    Page<Job> findAllByCompanyAndTitleContainingAndAddressCompany_Location_NameCityContaining(String title, String nameCity, Company company, Pageable pageable);
List<Job> findByCompany(Company company);
}
