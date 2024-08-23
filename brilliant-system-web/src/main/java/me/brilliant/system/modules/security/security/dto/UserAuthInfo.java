package me.brilliant.system.modules.security.security.dto;

import lombok.Data;

import java.util.Set;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/15
 */
@Data
public class UserAuthInfo {

    private Long userId;

    private String username;

    private String nickname;

    private Long deptId;

    private String password;

    private Integer status;

    private Set<String> roles;

    private Set<String> perms;

    private Integer dataScope;

    private Boolean admin;

}
