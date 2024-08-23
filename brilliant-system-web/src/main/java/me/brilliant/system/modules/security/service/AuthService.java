package me.brilliant.system.modules.security.service;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.brilliant.system.modules.security.model.AuthUserResponseDto;
import me.brilliant.system.modules.security.model.LoginResult;
import me.brilliant.system.modules.security.security.OnlineUserService;
import me.brilliant.system.modules.security.security.bean.LoginProperties;
import me.brilliant.system.modules.security.security.bean.SecurityProperties;
import me.brilliant.system.modules.security.security.core.TokenProvider;
import me.brilliant.system.modules.security.security.dto.JwtUserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author Star Chou
 * @description /
 * @create 2024/8/22
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final SecurityProperties properties;
    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Resource
    private LoginProperties loginProperties;

    public AuthUserResponseDto login(String username, String password, HttpServletRequest request) {
        // 密码解密
        //RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, authUser.getPassword());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
        if (loginProperties.isSingleLogin()) {
            // 踢掉之前已经登录的token
            onlineUserService.kickOutForUsername(username);
        }
        // 保存在线信息
        onlineUserService.save(jwtUserDto, token, request);
        // 返回 token 与 用户信息
        return new AuthUserResponseDto(properties.getTokenStartWith() + token, jwtUserDto);

    }

    /**
     * 登出
     *
     * @param request
     */
    public void logout(HttpServletRequest request) {
        onlineUserService.logout(tokenProvider.getToken(request));
    }
}
