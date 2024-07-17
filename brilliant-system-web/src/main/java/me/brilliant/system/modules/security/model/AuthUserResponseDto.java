package me.brilliant.system.modules.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.brilliant.boot.web.security.dto.JwtUserDto;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserResponseDto {

    private String token;
    private JwtUserDto jwtUserDto;
}
