package com.agilent.iad.service;

import com.agilent.iad.model.User;

import java.util.List;

/**
 * @author lifang
 * @since 2023-07-19
 */
public interface UserService {

    List<User> doFindAll();

    User doFindByName(String userName);

    User doFindById(Long id);
}
