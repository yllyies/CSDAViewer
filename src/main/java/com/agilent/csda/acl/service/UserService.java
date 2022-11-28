package com.agilent.csda.acl.service;

import com.agilent.csda.acl.model.User;

import java.util.List;

/**
 * @author lifang
 * @since 2022-11-28
 */
public interface UserService {

    List<User> doFindAll();

    List<User> doFindPage(int pageNum, int pageSize);

    User doFindByName(String userName);

    User doFindById(Long id);
}
