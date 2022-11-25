package com.agilent.csda.acl.service;

import com.agilent.csda.acl.model.User;

import java.util.List;

/**
 * @author lifang
 * @since 2019-08-29
 */
public interface UserService {

    int doCreate(User user);

    List<User> doFindAll();

    List<User> doFindPage(int pageNum, int pageSize);

    User doFindByName(String userName);

    User doFindById(Long id);

    int doUpdate(User user);

    int doDelete(Long userId);


}
