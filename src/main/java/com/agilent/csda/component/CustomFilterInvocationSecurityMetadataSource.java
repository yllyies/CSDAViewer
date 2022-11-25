package com.agilent.csda.component;

import com.agilent.csda.acl.model.Role;
import com.agilent.csda.acl.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Custom FilterInvocationSecurityMetadataSource
 */
@Component("customFilterInvocationSecurityMetadataSource")
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private RoleService roleService;

    private static HashMap<String, Collection<ConfigAttribute>> map = null;

    /**
     * Permission (role) set required for each resource (url)
     */
    private void getResourcePermission() {
        map = new HashMap<>();
        List<Role> roles = roleService.doFindAll();

        for (Role role : roles) {
            ConfigAttribute configAttribute = new SecurityConfig(role.getAuthority());
            List<ConfigAttribute> urlRoles = new ArrayList<>();
            urlRoles.add(configAttribute);
            map.put("/to" + role.getName(), urlRoles);
        }
    }

    /**
     * Get the set of permissions (roles) required for a specific resource (url) requested by the user
     *
     * @param object contains the request information requested by the user
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if (map == null) {
            getResourcePermission();
        }
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();

        /**
         * Traversing each resource , if it matches the resource requested by the user,
         * it returns the set of permissions required by the resource.
         * If none of them match, it indicates that the resource requested by the user is not need permission to access
         */
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String url = iter.next();
            if (new AntPathRequestMatcher(url).matches(request)) {
                return map.get(url);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
