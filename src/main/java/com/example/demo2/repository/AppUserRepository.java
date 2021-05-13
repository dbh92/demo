package com.example.demo2.repository;

import com.example.demo2.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByName(String username);

}
