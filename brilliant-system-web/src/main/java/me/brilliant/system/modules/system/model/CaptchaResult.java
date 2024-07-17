package me.brilliant.system.modules.system.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/11
 */
@Schema(description = "验证码响应对象")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaptchaResult {

    @Schema(description = "验证码ID")
    private String captchaKey;

    @Schema(description = "验证码图片Base64字符串")
    private String captchaBase64;

}
