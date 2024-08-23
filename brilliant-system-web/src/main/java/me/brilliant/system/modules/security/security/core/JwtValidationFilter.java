package me.brilliant.system.modules.security.security.core;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import me.brilliant.system.modules.security.security.OnlineUserService;
import me.brilliant.system.modules.security.security.UserCacheManager;
import me.brilliant.system.modules.security.security.bean.SecurityProperties;
import me.brilliant.system.modules.security.security.dto.OnlineUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Star Chou
 * @description JWT token 校验过滤器
 * @create 2024/7/11
 */
public class JwtValidationFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(JwtValidationFilter.class);

    private final TokenProvider tokenProvider;
    private final SecurityProperties properties;
    private final OnlineUserService onlineUserService;
    private final UserCacheManager userCacheManager;

    /**
     * @param tokenProvider     Token
     * @param properties        JWT
     * @param onlineUserService 用户在线
     * @param userCacheManager    用户缓存工具
     */
    public JwtValidationFilter(TokenProvider tokenProvider, SecurityProperties properties, OnlineUserService onlineUserService, UserCacheManager userCacheManager) {
        this.properties = properties;
        this.onlineUserService = onlineUserService;
        this.tokenProvider = tokenProvider;
        this.userCacheManager = userCacheManager;
    }

    /**
     * 从请求中获取 JWT Token，校验 JWT Token 是否合法
     * <p>
     * 如果合法则将 Authentication 设置到 Spring Security Context 上下文中
     * 如果不合法则清空 Spring Security Context 上下文，并直接返回响应
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = resolveToken(httpServletRequest);
        // 对于 Token 为空的不需要去查 Redis
        if (StrUtil.isNotBlank(token)) {
            OnlineUserDto onlineUserDto = null;
            boolean cleanUserCache = false;
            try {
                String loginKey = tokenProvider.loginKey(token);
                onlineUserDto = onlineUserService.getOne(loginKey);
            } catch (ExpiredJwtException e) {
                log.error(e.getMessage());
                cleanUserCache = true;
            } finally {
                if (cleanUserCache || Objects.isNull(onlineUserDto)) {
                    userCacheManager.cleanUserCache(String.valueOf(tokenProvider.getClaims(token).get(TokenProvider.AUTHORITIES_KEY)));
                }
            }
            if (onlineUserDto != null && StringUtils.hasText(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // Token 续期
                tokenProvider.checkRenewal(token);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 初步检测Token
     *
     * @param request /
     * @return /
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(properties.getHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(properties.getTokenStartWith())) {
            // 去掉令牌前缀
            return bearerToken.replace(properties.getTokenStartWith(), "");
        } else {
            log.debug("非法Token：{}", bearerToken);
        }
        return null;
    }
}
