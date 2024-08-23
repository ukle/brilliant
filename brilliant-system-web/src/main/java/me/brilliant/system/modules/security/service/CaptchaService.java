package me.brilliant.system.modules.security.service;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import me.brilliant.boot.web.utils.RedisUtils;
import me.brilliant.system.modules.security.security.bean.CaptchaProperties;
import me.brilliant.system.modules.security.security.constants.CaptchaTypeEnum;
import me.brilliant.system.modules.security.model.CaptchaResult;
import me.brilliant.system.modules.security.security.constants.SecurityConstants;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Star Chou
 * @description /
 * @create 2024/8/22
 */
@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final CaptchaProperties captchaProperties;
    private final CodeGenerator codeGenerator;
    private final Font captchaFont;

    private final RedisUtils redisUtils;

    /**
     * 生成验证码
     *
     * @return
     */
    public CaptchaResult getCaptcha() {
        String captchaType = captchaProperties.getType();
        int width = captchaProperties.getWidth();
        int height = captchaProperties.getHeight();
        int interfereCount = captchaProperties.getInterfereCount();
        int codeLength = captchaProperties.getCode().getLength();

        AbstractCaptcha captcha;
        if (CaptchaTypeEnum.CIRCLE.name().equalsIgnoreCase(captchaType)) {
            captcha = CaptchaUtil.createCircleCaptcha(width, height, codeLength, interfereCount);
        } else if (CaptchaTypeEnum.GIF.name().equalsIgnoreCase(captchaType)) {
            captcha = CaptchaUtil.createGifCaptcha(width, height, codeLength);
        } else if (CaptchaTypeEnum.LINE.name().equalsIgnoreCase(captchaType)) {
            captcha = CaptchaUtil.createLineCaptcha(width, height, codeLength, interfereCount);
        } else if (CaptchaTypeEnum.SHEAR.name().equalsIgnoreCase(captchaType)) {
            captcha = CaptchaUtil.createShearCaptcha(width, height, codeLength, interfereCount);
        } else {
            throw new IllegalArgumentException("Invalid captcha type: " + captchaType);
        }
        captcha.setGenerator(codeGenerator);
        captcha.setTextAlpha(captchaProperties.getTextAlpha());
        captcha.setFont(captchaFont);

        String captchaCode = captcha.getCode();
        String imageBase64Data = captcha.getImageBase64Data();

        // 验证码文本缓存至Redis，用于登录校验
        String captchaKey = IdUtil.fastSimpleUUID();
        redisUtils.set(SecurityConstants.CAPTCHA_CODE_PREFIX + captchaKey, captchaCode,
                captchaProperties.getExpireSeconds(), TimeUnit.SECONDS);

        return CaptchaResult.builder()
                .captchaKey(captchaKey)
                .captchaBase64(imageBase64Data)
                .build();
    }
}
