package com.example.ojt.repository;

import com.example.ojt.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICompanyRepository extends JpaRepository<Company,Integer> {
    boolean existsByPhone(String phone);
    boolean existsByName(String name);
    Optional<Company> findByAccountId(Integer id);

}
