package com.agilent.csda.component;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 * Custom AccessDecisionManager
 */
@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {

    /**
     * decide have authority to access
     *
     * @param auth             userDetails info
     * @param request          request info
     * @param configAttributes the authority role collection to access url by CustomFilterInvocationSecurityMetadataSource.getAttributes(object)
     */
    @Override
    public void decide(Authentication auth, Object request, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        /**
         * If the requested resource don't require permission, it will be released directly.
         */
        if (configAttributes == null || configAttributes.size() <= 0) {
            return;
        }
        /**
         * determine whether the permission owned by the user is one of the permissions required by the resource,
         * and if so,release it, otherwise intercept
         */
        Iterator<ConfigAttribute> iter = configAttributes.iterator();
        while (iter.hasNext()) {
            String needRole = iter.next().getAttribute();
            for (GrantedAuthority grantRole : auth.getAuthorities()) {
                if (needRole.trim().equals(grantRole.getAuthority().trim())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("no privilege");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
