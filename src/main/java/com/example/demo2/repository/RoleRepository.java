package com.example.demo2.repository;

import com.example.demo2.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role,Long> {
    Role getById(Long id);
}
