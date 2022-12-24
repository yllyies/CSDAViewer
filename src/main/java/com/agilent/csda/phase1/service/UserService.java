package com.agilent.csda.phase1.service;

import com.agilent.csda.phase1.model.User;

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
