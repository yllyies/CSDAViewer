package com.agilent.csda.acl.service.impl;

import com.agilent.csda.acl.service.UserRolesService;
import com.github.pagehelper.PageHelper;
import com.agilent.csda.acl.dto.UserRolesDto;
import com.agilent.csda.acl.mapper.UserMapper;
import com.agilent.csda.acl.model.Role;
import com.agilent.csda.acl.model.User;
import com.agilent.csda.acl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lifang
 * @since 2019-09-01
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Resource
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRolesService userRolesService;

    @Override
    public int doCreate(User user) {
        user.setId(1L);
        user.setSaml("1");
        user.setStatus("1");
        return userMapper.insert(user);
    }

    @Override
    public List<User> doFindAll() {
        return userMapper.selectAll();
    }

    @Override
    public List<User> doFindPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return userMapper.selectAll();
    }

    @Override
    public User doFindByName(String userName) {
        return userMapper.selectByUserName(userName);
    }

    @Override
    public User doFindById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public int doUpdate(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int doDelete(Long userId) {
        // delete user role relationship
        userRolesService.doDelete(userId);
        return userMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.doFindByName(username);
        if (user != null) {
            UserRolesDto userRolesDto = userRolesService.doFindByUserId(user.getId());
            userRolesDto.setAclUserName(user.getName());
            userRolesDto.setAclUser(user);
            if (userRolesDto == null) {
                throw new UsernameNotFoundException(String.format("用户'%s'不存在", username));
            }
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for (Role role : userRolesDto.getAclRoleList()) {
                //封装用户信息和角色信息到SecurityContextHolder全局缓存中
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));
            }
            return userRolesDto;
        } else {
            throw new UsernameNotFoundException("admin: " + username + " do not exist!");
        }
    }
}
