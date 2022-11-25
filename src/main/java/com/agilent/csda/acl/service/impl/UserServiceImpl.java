package com.agilent.csda.acl.service.impl;

import com.agilent.csda.acl.dao.UserDao;
import com.github.pagehelper.PageHelper;
import com.agilent.csda.acl.dto.UserRolesDto;
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
import java.util.Arrays;
import java.util.List;

/**
 * @author lifang
 * @since 2019-09-01
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Resource
    private UserDao userDao;
    @Autowired
    private UserService userService;

    @Override
    public int doCreate(User user) {
        user.setId(1L);
        user.setSaml("1");
        user.setStatus("1");
        userDao.save(user);
        return 1;
    }

    @Override
    public List<User> doFindAll() {
        return userDao.findAll();
    }

    @Override
    public List<User> doFindPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return userDao.findAll();
    }

    @Override
    public User doFindByName(String userName) {
        return userDao.findByName(userName).get(0);
    }

    @Override
    public User doFindById(Long id) {
        return userDao.findById(id).get();
    }

    @Override
    public int doUpdate(User user) {
        userDao.save(user);
        return 1;
    }

    @Override
    public int doDelete(Long userId) {
        userDao.deleteById(userId);
        return 1;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.doFindByName(username);
        if (user != null) {
            UserRolesDto userRolesDto = new UserRolesDto();
            Role role1 = new Role();
            role1.setAuthority("1");
            userRolesDto.setAclRoleList(Arrays.asList(role1));
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
