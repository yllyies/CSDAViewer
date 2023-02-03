package com.agilent.cdsa.service;

import com.agilent.cdsa.model.User;

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
