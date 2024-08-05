package com.example.ojt.repository;


import com.example.ojt.model.entity.Role;
import com.example.ojt.model.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findAllById(long id);
    Optional<Role> findByRoleName(RoleName name);
}
