package com.example.demo2.service.roleService;

import com.example.demo2.model.Role;
import com.example.demo2.service.GeneralService;

public interface RoleService extends GeneralService<Role> {

    Role getById(Long id);
}
