package me.brilliant.system.modules.security.security.bean;

import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

/**
 * @author Star Chou
 * @description /
 * @create 2024/8/22
 */
@Configuration
public class CaptchaConfig {

    @Autowired
    private CaptchaProperties captchaProperties;

    /**
     * 验证码文字生成器
     *
     * @return CodeGenerator
     */
    @Bean
    public CodeGenerator codeGenerator() {
        String codeType = captchaProperties.getCode().getType();
        int codeLength = captchaProperties.getCode().getLength();
        if ("math".equalsIgnoreCase(codeType)) {
            return new MathGenerator(codeLength);
        } else if ("random".equalsIgnoreCase(codeType)) {
            return new RandomGenerator(codeLength);
        } else {
            throw new IllegalArgumentException("Invalid captcha generator type: " + codeType);
        }
    }

    /**
     * 验证码字体
     */
    @Bean
    public Font captchaFont() {
        String fontName = captchaProperties.getFont().getName();
        int fontSize = captchaProperties.getFont().getSize();
        int fontWight = captchaProperties.getFont().getWeight();
        return new Font(fontName, fontWight, fontSize);
    }

}

