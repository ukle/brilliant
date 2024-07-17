package me.brilliant.system.modules.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import me.brilliant.system.modules.system.model.entity.SysUser;

import java.util.List;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/10
 */
@Data
public class SysUserForCreateDto {

    @Schema(description = "账号")
    @NotEmpty(message = "账号必填")
    @Size(max = 30, message = "账号长度不能超过30")
    private String username;

    @Schema(description = "姓名")
    @NotEmpty(message = "姓名必填")
    @Size(max = 30, message = "姓名长度不能超过30")
    private String name;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "登录密码", hidden = true)
    private String password;

    @Schema(description = "状态（1启用，0禁用）")
    private Integer status;

    @Schema(description = "绑定角色")
    private List<Long> roleIds;

    public SysUser convertToSysUser() {
        SysUser sysUser = new SysUser();
        sysUser.setStatus(this.getStatus());
        sysUser.setUsername(this.getUsername());
        sysUser.setPhone(this.getPhone());
        sysUser.setName(this.getName());
        sysUser.setPassword(this.getPassword());
        sysUser.setAdmin(false);
        return sysUser;
    }
}
