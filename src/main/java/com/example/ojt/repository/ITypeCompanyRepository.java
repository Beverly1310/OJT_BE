package com.example.ojt.repository;

import com.example.ojt.model.entity.TypeCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITypeCompanyRepository extends JpaRepository<TypeCompany,Integer> {
}
