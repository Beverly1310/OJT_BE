package com.example.ojt.repository;

import java.util.Optional;

import com.example.ojt.model.entity.Company;
import com.example.ojt.model.entity.TypeCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ICompanyRepository extends JpaRepository<Company, Integer> {

    boolean existsByPhone(String phone);

    boolean existsByName(String name);

    Optional<Company> findByAccountId(Integer id);

    @Query("SELECT c FROM Company c " +
            "JOIN c.addressCompanySet a " +
            "JOIN a.location l " +
            "WHERE (:companyName IS NULL OR c.name LIKE CONCAT(:companyName, '%')) " +
            "AND (:locationName IS NULL OR l.nameCity LIKE CONCAT(:locationName, '%'))")
    Page<Company> findAllByCompanyNameAndLocation(@Param("companyName") String companyName,
                                                  @Param("locationName") String locationName,
                                                  Pageable pageable);

    List<Company> findByTypeCompany(TypeCompany typeCompany);

    @Query("select cp from Company cp where  cp.Outstanding=:outstanding and cp.account.status=1")
    List<Company> getCompanyByOutstanding(Integer outstanding);
}

