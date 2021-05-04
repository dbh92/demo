package com.example.demo2.service.appUserService;

import com.example.demo2.model.AppUser;
import com.example.demo2.model.Role;
import com.example.demo2.repository.IAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService, UserDetailsService {
    @Autowired
    private IAppUserRepository appUserRepository;

    @Override
    public AppUser getUserByUsername(String username) {
        return appUserRepository.findByName(username);
    }

    @Override
    public Iterable<AppUser> getAllByRoleId(Long id) {
        return appUserRepository.getAllByRoleId(id);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = this.getUserByUsername(username);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(user.getRole());

        UserDetails userDetails = new User(
                user.getName(),
                user.getPassword(),
                authorities);
        return userDetails;
    }

    @Override
    public AppUser getCurrentUser() {
        AppUser user;
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        user = this.getUserByUsername(userName);
        return user;
    }

    @Override
    public Iterable<AppUser> getAllByRoleIsNotContaining(Long id) {
        return appUserRepository.getAllByRoleIsNotContaining(id);
    }

    @Override
    public Iterable<AppUser> getAllByRoleOrRole(Role role1, Role role2) {
        return appUserRepository.getAllByRoleOrRole(role1, role2);
    }

    @Override
    public Iterable<AppUser> getAllByNameIsContaining(String name) {
        return appUserRepository.getAllByNameIsContaining(name);
    }

    @Override
    public Iterable<AppUser> findAll() {
        return appUserRepository.findAll();
    }

    @Override
    public Optional<AppUser> findById(Long id) {
        return appUserRepository.findById(id);
    }

    @Override
    public AppUser save(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    @Override
    public void remove(Long id) {
        appUserRepository.deleteById(id);
    }


}