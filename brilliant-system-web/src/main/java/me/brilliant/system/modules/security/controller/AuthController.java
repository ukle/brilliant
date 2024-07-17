package me.brilliant.system.modules.security.controller;

import cn.hutool.core.util.IdUtil;
import com.wf.captcha.base.Captcha;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.brilliant.boot.web.annotation.AnonymousDeleteMapping;
import me.brilliant.boot.web.annotation.AnonymousGetMapping;
import me.brilliant.boot.web.annotation.AnonymousPostMapping;
import me.brilliant.boot.web.security.OnlineUserService;
import me.brilliant.boot.web.security.bean.LoginCodeEnum;
import me.brilliant.boot.web.security.bean.LoginProperties;
import me.brilliant.boot.web.security.bean.SecurityProperties;
import me.brilliant.boot.web.security.core.TokenProvider;
import me.brilliant.boot.web.security.dto.JwtUserDto;
import me.brilliant.system.modules.security.SecurityUtils;
import me.brilliant.system.modules.security.model.AuthUserRequestDto;
import me.brilliant.system.modules.security.model.AuthUserResponseDto;
import me.brilliant.system.modules.security.model.CodeResponseDto;
import me.brilliant.boot.web.mvc.BaseController;
import me.brilliant.boot.web.plugin.norepeat.annotation.PreventRepeatSubmit;
import me.brilliant.boot.web.utils.RedisUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 认证控制层
 */
@Tag(name = "认证中心")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController extends BaseController {

    private final SecurityProperties properties;
    private final RedisUtils redisUtils;
    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    @Resource
    private LoginProperties loginProperties;

    @Operation(summary = "登录授权")
    @AnonymousPostMapping(value = "/login")
    public AuthUserResponseDto login(@Validated @RequestBody AuthUserRequestDto authUser,
                                     HttpServletRequest request) throws Exception {
        // 密码解密
        String password = "123456";
        //RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, authUser.getPassword());
        /*
        // 查询验证码
        String code = (String) redisUtils.get(authUser.getUuid());
        // 清除验证码
        redisUtils.del(authUser.getUuid());
        if (StringUtils.isBlank(code)) {
            throw new BadRequestException("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
            throw new BadRequestException("验证码错误");
        }*/
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authUser.getUsername(), password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
        // 返回 token 与 用户信息
        AuthUserResponseDto responseDto = new AuthUserResponseDto(properties.getTokenStartWith() + token, jwtUserDto);
        if (loginProperties.isSingleLogin()) {
            // 踢掉之前已经登录的token
            onlineUserService.kickOutForUsername(authUser.getUsername());
        }
        // 保存在线信息
        onlineUserService.save(jwtUserDto, token, request);
        // 返回登录信息
        return responseDto;
    }

    @Operation(summary = "获取用户信息")
    @GetMapping(value = "/info")
    public ResponseEntity<UserDetails> getUserInfo() {
        return ResponseEntity.ok(SecurityUtils.getCurrentUser());
    }

    @PreventRepeatSubmit
    @Operation(summary = "获取验证码")
    @AnonymousGetMapping(value = "/code")
    public CodeResponseDto getCode() {
        // 获取运算的结果
        Captcha captcha = loginProperties.getCaptcha();
        String uuid = properties.getCodeKey() + IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == LoginCodeEnum.ARITHMETIC.ordinal() && captchaValue.contains(".")) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 保存
        redisUtils.set(uuid, captchaValue, loginProperties.getLoginCode().getExpiration(), TimeUnit.MINUTES);
        // 验证码信息
        return new CodeResponseDto(captcha.toBase64(), uuid);
    }

    @Operation(summary = "退出登录")
    @AnonymousDeleteMapping(value = "/logout")
    public void logout(HttpServletRequest request) {
        onlineUserService.logout(tokenProvider.getToken(request));
    }

}
