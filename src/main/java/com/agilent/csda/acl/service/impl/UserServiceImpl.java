package com.agilent.csda.acl.service.impl;

import com.agilent.csda.acl.dao.UserDao;
import com.github.pagehelper.PageHelper;
import com.agilent.csda.acl.dto.UserRolesDto;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.doFindByName(username);
        if (user != null) {
            UserRolesDto userRolesDto = new UserRolesDto();
            userRolesDto.setAclUserName(user.getName());
            userRolesDto.setAclUser(user);
            List<String> userRoles = new ArrayList<String>();
            userRoles.add("ROLE_ADMIN");
            if (userRolesDto == null) {
                throw new UsernameNotFoundException(String.format("用户'%s'不存在", username));
            }
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for (String roleName : userRoles) {
                //将角色也添加到权限之中
                grantedAuthorities.add(new SimpleGrantedAuthority(roleName));
//            grantedAuthorities.add(new SimpleGrantedAuthority("appLogs:read"));
//            grantedAuthorities.add(new SimpleGrantedAuthority(String.format("%s:%s", "appUsers", "read")));
//            grantedAuthorities.add(new SimpleGrantedAuthority(String.format("%s:%s", "industry", "read")));
            }
            return userRolesDto;
        } else {
            throw new UsernameNotFoundException("admin: " + username + " do not exist!");
        }
    }
}
