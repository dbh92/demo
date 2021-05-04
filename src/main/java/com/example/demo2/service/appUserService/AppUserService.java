package com.example.demo2.service.appUserService;

import com.example.demo2.model.AppUser;
import com.example.demo2.model.Role;
import com.example.demo2.service.GeneralService;

public interface AppUserService extends GeneralService<AppUser> {

    AppUser getUserByUsername(String username);

    Iterable<AppUser> getAllByRoleId(Long id);

    AppUser getCurrentUser();

    Iterable<AppUser> getAllByRoleIsNotContaining(Long id);

    Iterable<AppUser> getAllByNameIsContaining(String name);

    Iterable<AppUser> getAllByRoleOrRole(Role role1, Role role2);
}