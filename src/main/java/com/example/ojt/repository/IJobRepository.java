package com.example.ojt.repository;

import com.example.ojt.model.entity.Company;
import com.example.ojt.model.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface IJobRepository extends JpaRepository<Job,Integer> {
    Page<Job> findAllByTitleContainsAndAddressCompany_Location_NameCityContains(String title, String nameCity, Pageable pageable);
Page<Job> findAllByTitleContains(String title , Pageable pageable);
Optional<Job> findByTitle(String title);
Optional<Job> findByIdAndCompany(Integer id, Company company);

    @Query("SELECT j FROM Job j JOIN TypesJobs tj ON j.id = tj.job.id WHERE tj.typeJob.name IN :typeNames")
    List<Job> findByTypesJobs_NameIn(@Param("typeNames") Set<String> typeNames);

    @Query("SELECT j FROM Job j " +
            "WHERE j.company = :company " +
            "AND (:title IS NULL OR j.title LIKE %:title%) " +
            "AND (:location IS NULL OR j.addressCompany.location.nameCity LIKE %:location%)")
    Page<Job> findAllByCompanyAndTitleContainingAndLocationContaining(
            @Param("company") Company company,
            @Param("title") String title,
            @Param("location") String location,
            Pageable pageable);

    Page<Job> findAllByCompany(Company company , Pageable pageable);
}
