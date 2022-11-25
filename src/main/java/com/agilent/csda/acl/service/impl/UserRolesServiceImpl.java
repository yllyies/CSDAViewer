package com.agilent.csda.acl.service.impl;

import com.github.pagehelper.PageHelper;
import com.agilent.csda.acl.dto.UserRolesDto;
import com.agilent.csda.acl.mapper.UserRolesMapper;
import com.agilent.csda.acl.model.UserRole;
import com.agilent.csda.acl.service.UserRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lifang
 * @since 2019-09-03
 */
@Service
public class UserRolesServiceImpl implements UserRolesService {

    @Autowired
    private UserRolesMapper userRolesMapper;

    @Override
    public int doCreate(UserRole userRoles) {
        return userRolesMapper.insert(userRoles);
    }

    @Override
    public List<UserRolesDto> doFindDtoPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return userRolesMapper.selectGroupByUserId();
    }

    @Override
    public UserRolesDto doFindByUserId(Long userId) {
        return userRolesMapper.selectByUserId(userId);
    }

    @Override
    public int doUpdate(UserRole userRoles) {
        return userRolesMapper.updateByUserIdAndRoleId(userRoles);
    }

    @Override
    public int doDelete(Long userId) {
        return userRolesMapper.deleteByUserId(userId);
    }
}
