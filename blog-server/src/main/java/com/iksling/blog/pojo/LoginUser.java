package com.iksling.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails {
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 登录平台
     */
    private Boolean loginPlatform;

    /**
     * 角色idList
     */
    private List<String> roleIdList;

    /**
     * 角色权重
     */
    private Integer roleWeight;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roleIdList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginUser loginUser = (LoginUser) o;
        return Objects.equals(userId, loginUser.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}