package com.example.demo2.service;

import com.example.demo2.model.AppUser;

public interface AppUserService extends GeneralService<AppUser> {

    AppUser getUserByUsername(String username);

}