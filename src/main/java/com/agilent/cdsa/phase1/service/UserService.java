package com.agilent.cdsa.phase1.service;

import com.agilent.cdsa.phase1.model.User;

import java.util.List;

/**
 * @author lifang
 * @since 2022-11-28
 */
public interface UserService {

    List<User> doFindAll();

    User doFindByName(String userName);

    User doFindById(Long id);
}
