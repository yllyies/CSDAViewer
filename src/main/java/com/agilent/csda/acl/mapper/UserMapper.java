package com.agilent.csda.acl.mapper;

import com.agilent.csda.acl.model.User;

import java.util.List;

public interface UserMapper {

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    List<User> selectAll();

    User selectByUserName(String userName);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int deleteByPrimaryKey(Long id);
}