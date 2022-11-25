package com.agilent.csda.acl.service;

import com.agilent.csda.acl.model.Role;

import java.util.List;

/**
 * @author lifang
 * @since 2019-09-01
 */
public interface RoleService {

    int doCreate(Role role);

    List<Role> doFindPage(int pageNum, int pageSize);

    Role doFindById(Long id);

    int doUpdate(Role role);

    int doDelete(Long userId);

    List<Role> doFindAll();
}
