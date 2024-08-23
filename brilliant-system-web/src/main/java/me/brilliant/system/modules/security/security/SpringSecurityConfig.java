package me.brilliant.system.modules.security.security;

import cn.hutool.captcha.generator.CodeGenerator;
import lombok.RequiredArgsConstructor;
import me.brilliant.boot.web.annotation.AnonymousAccess;
import me.brilliant.boot.web.enums.RequestMethodEnum;
import me.brilliant.boot.web.utils.RedisUtils;
import me.brilliant.system.modules.security.security.bean.SecurityProperties;
import me.brilliant.system.modules.security.security.core.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/9
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final TokenProvider tokenProvider;
    private final OnlineUserService onlineUserService;
    private final UserCacheManager userCacheManager;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final SecurityProperties securityProperties;
    private final RedisUtils redisUtils;

    private final CodeGenerator codeGenerator;
    private final ApplicationContext applicationContext;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager 手动注入
     *
     * @param authenticationConfiguration 认证配置
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 搜寻匿名标记 url： @AnonymousAccess
        RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        // 获取匿名标记
        Map<String, Set<String>> anonymousUrls = getAnonymousUrl(handlerMethodMap);

        http
                .authorizeHttpRequests(requestMatcherRegistry ->
                        requestMatcherRegistry
                                // 自定义匿名访问所有url放行：允许匿名和带Token访问，细腻化到每个 Request 类型
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers(HttpMethod.GET, anonymousUrls.get(RequestMethodEnum.GET.getType()).toArray(new String[0])).permitAll()
                                .requestMatchers(HttpMethod.POST, anonymousUrls.get(RequestMethodEnum.POST.getType()).toArray(new String[0])).permitAll()
                                .requestMatchers(HttpMethod.PUT, anonymousUrls.get(RequestMethodEnum.PUT.getType()).toArray(new String[0])).permitAll()
                                .requestMatchers(HttpMethod.PATCH, anonymousUrls.get(RequestMethodEnum.PATCH.getType()).toArray(new String[0])).permitAll()
                                .requestMatchers(HttpMethod.DELETE, anonymousUrls.get(RequestMethodEnum.DELETE.getType()).toArray(new String[0])).permitAll()
                                .requestMatchers(anonymousUrls.get(RequestMethodEnum.ALL.getType()).toArray(new String[0])).permitAll()
                                // 放行的url
                                .requestMatchers(securityProperties.getIgnoreUrls().toArray(new String[0])).permitAll()
                                // 所有请求都需要认证
                                .anyRequest().authenticated()
                )
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        ;

        // 验证码校验过滤器
        http.addFilterBefore(new CaptchaValidationFilter(redisUtils, codeGenerator), UsernamePasswordAuthenticationFilter.class);
        // JWT 校验过滤器
        http.addFilterBefore(new JwtValidationFilter(tokenProvider, securityProperties,
                onlineUserService, userCacheManager), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private Map<String, Set<String>> getAnonymousUrl(Map<RequestMappingInfo, HandlerMethod> handlerMethodMap) {
        Map<String, Set<String>> anonymousUrls = new HashMap<>(8);
        Set<String> get = new HashSet<>();
        Set<String> post = new HashSet<>();
        Set<String> put = new HashSet<>();
        Set<String> patch = new HashSet<>();
        Set<String> delete = new HashSet<>();
        Set<String> all = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            AnonymousAccess anonymousAccess = handlerMethod.getMethodAnnotation(AnonymousAccess.class);
            if (null != anonymousAccess) {
                List<RequestMethod> requestMethods = new ArrayList<>(infoEntry.getKey().getMethodsCondition().getMethods());
                RequestMethodEnum request = RequestMethodEnum.find(requestMethods.size() == 0 ? RequestMethodEnum.ALL.getType() : requestMethods.get(0).name());
                switch (Objects.requireNonNull(request)) {
                    case GET:
                        get.addAll(infoEntry.getKey().getPathPatternsCondition().getPatternValues());
                        break;
                    case POST:
                        post.addAll(infoEntry.getKey().getPathPatternsCondition().getPatternValues());
                        break;
                    case PUT:
                        put.addAll(infoEntry.getKey().getPathPatternsCondition().getPatternValues());
                        break;
                    case PATCH:
                        patch.addAll(infoEntry.getKey().getPathPatternsCondition().getPatternValues());
                        break;
                    case DELETE:
                        delete.addAll(infoEntry.getKey().getPathPatternsCondition().getPatternValues());
                        break;
                    default:
                        all.addAll(infoEntry.getKey().getPathPatternsCondition().getPatternValues());
                        break;
                }
            }
        }
        anonymousUrls.put(RequestMethodEnum.GET.getType(), get);
        anonymousUrls.put(RequestMethodEnum.POST.getType(), post);
        anonymousUrls.put(RequestMethodEnum.PUT.getType(), put);
        anonymousUrls.put(RequestMethodEnum.PATCH.getType(), patch);
        anonymousUrls.put(RequestMethodEnum.DELETE.getType(), delete);
        anonymousUrls.put(RequestMethodEnum.ALL.getType(), all);
        return anonymousUrls;
    }

}

