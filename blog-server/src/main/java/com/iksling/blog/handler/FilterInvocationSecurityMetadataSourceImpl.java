package com.iksling.blog.handler;

import com.iksling.blog.pojo.ResourceRole;
import com.iksling.blog.mapper.ResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

@Component
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

    private static List<ResourceRole> resourceRoleList;

    @Autowired
    private ResourceMapper resourceMapper;

    @PostConstruct
    private void loadDataSource() {
        resourceRoleList = resourceMapper.listResourceRoles();
    }

    public void clearMetadataSource() {
        resourceRoleList = null;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if (CollectionUtils.isEmpty(resourceRoleList)) {
            this.loadDataSource();
            if (CollectionUtils.isEmpty(resourceRoleList))
                throw new AuthorizationServiceException("服务器繁忙, 请稍后再试!");
        }
        FilterInvocation fi = (FilterInvocation) object;
        String method = fi.getRequest().getMethod();
        String uri = fi.getRequest().getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (ResourceRole resourceRole : resourceRoleList) {
            if (antPathMatcher.match(resourceRole.getResourceUri(), uri) && resourceRole.getRequestMethod().equals(method)) {
                if (resourceRole.getIsDisabled() == 1)
                    throw new AccessDeniedException("该请求已被禁用!");
                if (resourceRole.getIsAnonymous() == 1)
                    return null;
                List<String> roleList = resourceRole.getRoleList();
                return SecurityConfig.createList(roleList.toArray(new String[]{}));
            }
        }
        throw new AccessDeniedException("该请求已经被喵星人劫持了!");
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
