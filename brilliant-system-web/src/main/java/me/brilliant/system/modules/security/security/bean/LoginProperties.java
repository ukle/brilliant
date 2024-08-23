package me.brilliant.system.modules.security.security.bean;

import lombok.Data;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/15
 */
@Data
public class LoginProperties {

    /**
     * 账号单用户 登录
     */
    private boolean singleLogin = false;

    public static final String cacheKey = "user-login-cache:";

    public boolean isSingleLogin() {
        return singleLogin;
    }

}
