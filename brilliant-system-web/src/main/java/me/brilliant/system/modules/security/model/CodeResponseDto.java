package me.brilliant.system.modules.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/15
 */
@Data
public class CodeResponseDto {

    @Schema(description = "图片base64")
    private String img;

    @Schema(description = "验证码结果")
    private String code;

    public CodeResponseDto(String img, String code) {
        this.img = img;
        this.code = code;
    }
}
