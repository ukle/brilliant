package me.brilliant.system.modules.security.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/15
 */
@Data
public class AuthUserRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String code;

    private String uuid = "";
}

