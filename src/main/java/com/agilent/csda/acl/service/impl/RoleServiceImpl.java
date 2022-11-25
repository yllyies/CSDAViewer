package com.agilent.csda.acl.service.impl;

import com.github.pagehelper.PageHelper;
import com.agilent.csda.acl.mapper.RoleMapper;
import com.agilent.csda.acl.model.Role;
import com.agilent.csda.acl.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lifang
 * @since 2019-09-03
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public int doCreate(Role role) {
        return roleMapper.insert(role);
    }

    @Override
    public List<Role> doFindPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return roleMapper.selectAll();
    }

    @Override
    public Role doFindById(Long id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int doUpdate(Role role) {
        return roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public int doDelete(Long userId) {
        return roleMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public List<Role> doFindAll() {
        return roleMapper.selectAll();
    }
}
