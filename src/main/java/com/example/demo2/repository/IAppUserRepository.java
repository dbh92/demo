package com.example.demo2.repository;

import com.example.demo2.model.AppUser;
import com.example.demo2.model.Role;
import org.springframework.data.repository.CrudRepository;


public interface IAppUserRepository extends CrudRepository<AppUser, Long> {

    AppUser findByName(String username);

    Iterable<AppUser> getAllByRoleId(Long id);

    Iterable<AppUser> getAllByRoleIsNotContaining(Long id);

    Iterable<AppUser> getAllByRoleOrRole(Role role1, Role role2);

    Iterable<AppUser> getAllByNameIsContaining(String name);
}
