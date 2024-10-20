package com.iksling.blog.handler;

import com.iksling.blog.mapper.ResourceMapper;
import com.iksling.blog.pojo.ResourceRole;
import com.iksling.blog.util.LocaleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

@Component
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

    private static List<ResourceRole> resourceRoleList;

    @Autowired
    private ResourceMapper resourceMapper;

    @PostConstruct
    public void loadResourceRoleList() {
        resourceRoleList = resourceMapper.getResourceRole();
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        String method = fi.getRequest().getMethod();
        String uri = fi.getRequest().getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (ResourceRole resourceRole : resourceRoleList) {
            if (antPathMatcher.match(resourceRole.getResourceUri(), uri) && resourceRole.getResourceRequestMethod().equals(method)) {
                if (resourceRole.getDisabledFlag())
                    throw new AccessDeniedException(LocaleUtil.getMessage("H0003"));
                if (resourceRole.getAnonymousFlag())
                    return null;
                if (resourceRole.getRoleIdList() == null)
                    throw new AccessDeniedException(LocaleUtil.getMessage("H0004"));
                return SecurityConfig.createList(resourceRole.getRoleIdList().split(","));
            }
        }
        throw new AccessDeniedException(LocaleUtil.getMessage("H0005"));
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }

}
