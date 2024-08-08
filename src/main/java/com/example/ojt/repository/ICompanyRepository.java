package com.example.ojt.repository;

import com.example.ojt.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICompanyRepository extends JpaRepository<Company,Integer> {
    boolean existsByPhone(String phone);
    boolean existsByName(String name);
    Company findByAccountId(Integer id);

}
