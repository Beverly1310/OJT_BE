package com.example.ojt.repository;

import com.example.ojt.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account,Integer> {
    ;
    boolean existsByEmail(String email);

    Optional <Account> findByEmail(String email);
}
