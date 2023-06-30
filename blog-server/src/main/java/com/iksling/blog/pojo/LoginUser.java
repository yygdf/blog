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
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 登录类型
     */
    private Integer loginType;

    /**
     * 登录设备
     */
    private String loginDevice;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * IP来源
     */
    private String ipSource;

    /**
     * 0未锁定，1已锁定
     */
    private Integer isLocked;

    /**
     * 0未禁用，1已禁用
     */
    private Integer isDisabled;

    /**
     * 用户角色
     */
    private List<String> roleList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roleList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isLocked == 0;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isDisabled == 0;
    }
}