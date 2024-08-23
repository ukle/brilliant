package me.brilliant.system.modules.security.security.dto;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtUserDto implements UserDetails {

    private Long userId;

    private Long deptId;

    private String username;

    private String nickName;

    private String email;

    private String phone;

    private String gender;

    private String password;

    private Boolean enabled;

    private Boolean isAdmin = false;

//    private List<Long> dataScopes;

    private Collection<SimpleGrantedAuthority> authorities;

    public JwtUserDto(UserAuthInfo user) {
        this.userId = user.getUserId();
        Set<String> roles = user.getRoles();
        Set<SimpleGrantedAuthority> authorities;
        if (CollectionUtil.isNotEmpty(roles)) {
            authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // 标识角色
                    .collect(Collectors.toSet());
        } else {
            authorities = Collections.EMPTY_SET;
        }
        this.authorities = authorities;
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.enabled = ObjectUtil.equal(user.getStatus(), 1);
//        this.perms = user.getPerms();
        this.deptId = user.getDeptId();
//        this.dataScope = user.getDataScope();
        this.isAdmin = user.getAdmin();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}
