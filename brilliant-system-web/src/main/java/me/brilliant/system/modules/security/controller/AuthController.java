package me.brilliant.system.modules.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.brilliant.boot.web.annotation.AnonymousDeleteMapping;
import me.brilliant.boot.web.annotation.AnonymousGetMapping;
import me.brilliant.boot.web.annotation.AnonymousPostMapping;
import me.brilliant.boot.web.log.NeedLog;
import me.brilliant.boot.web.mvc.BaseController;
import me.brilliant.boot.web.plugin.norepeat.PreventRepeatSubmit;
import me.brilliant.system.modules.security.SecurityUtils;
import me.brilliant.system.modules.security.model.AuthUserResponseDto;
import me.brilliant.system.modules.security.model.CaptchaResult;
import me.brilliant.system.modules.security.service.AuthService;
import me.brilliant.system.modules.security.service.CaptchaService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制层
 */
@Tag(name = "认证中心")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController extends BaseController {

    private final AuthService authService;
    private final CaptchaService captchaService;

    @Operation(summary = "登录授权")
    @AnonymousPostMapping(value = "/login")
    public AuthUserResponseDto login(@Parameter(description = "用户名", example = "admin") @RequestParam String username,
                                     @Parameter(description = "密码", example = "123456") @RequestParam String password,
                                     HttpServletRequest request) {
        return authService.login(username, password, request);
    }

    @NeedLog
    @Operation(summary = "获取用户信息")
    @GetMapping(value = "/info")
    public UserDetails getUserInfo() {
        return SecurityUtils.getCurrentUser();
    }

    @PreventRepeatSubmit
    @Operation(summary = "获取验证码")
    @AnonymousGetMapping(value = "/captcha")
    public CaptchaResult getCaptcha() {
        return captchaService.getCaptcha();
    }

    @Operation(summary = "退出登录")
    @AnonymousDeleteMapping(value = "/logout")
    public void logout(HttpServletRequest request) {
        authService.logout(request);
    }

}
