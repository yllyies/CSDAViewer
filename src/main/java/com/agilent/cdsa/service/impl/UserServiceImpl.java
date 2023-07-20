package com.agilent.cdsa.service.impl;

import com.agilent.cdsa.common.dto.BusinessException;
import com.agilent.cdsa.license.client.LicenseVerify;
import com.agilent.cdsa.dto.UserRolesDto;
import com.agilent.cdsa.model.User;
import com.agilent.cdsa.repository.UserDao;
import com.agilent.cdsa.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
 * @since 2023-07-19
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    @Resource
    private UserDao userDao;
    @Autowired
    private UserService userService;
    @Resource
    private LicenseVerify licenseVerify;

    @Override
    public List<User> doFindAll() {
        return userDao.findAll();
    }

    @Override
    public User doFindByName(String userName) {
        return userDao.findByName(userName);
    }

    @Override
    public User doFindById(Long id) {
        return userDao.findById(id).get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 证书验证
        if (!licenseVerify.verify()) {
            log.warn("您的证书无效，请核查服务器是否取得授权或重新申请证书！");
            throw new BusinessException("您的证书无效，请核查服务器是否取得授权或重新申请证书！");
        }
        User user = userService.doFindByName(username);
        if (user != null) {
            UserRolesDto userRolesDto = new UserRolesDto();
            userRolesDto.setAclUserName(user.getName());
            userRolesDto.setAclUser(user);
            List<String> userRoles = new ArrayList<>();
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
            throw new UsernameNotFoundException("用户不存在 " + username);
        }
    }
}
