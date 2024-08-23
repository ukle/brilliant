package me.brilliant.system.modules.security.security.core;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.brilliant.boot.web.result.ResultCode;
import me.brilliant.boot.web.utils.ResponseUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/11
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        int status = response.getStatus();
        if (status == HttpServletResponse.SC_NOT_FOUND) {
            // 资源不存在
            ResponseUtils.writeErrMsg(response, ResultCode.RESOURCE_NOT_FOUND);
        } else {
            if (authException instanceof BadCredentialsException) {
                // 用户名或密码错误
                ResponseUtils.writeErrMsg(response, ResultCode.USERNAME_OR_PASSWORD_ERROR);
            } else {
                // 未认证或者token过期
                ResponseUtils.writeErrMsg(response, ResultCode.TOKEN_INVALID);
            }
        }
    }
}
