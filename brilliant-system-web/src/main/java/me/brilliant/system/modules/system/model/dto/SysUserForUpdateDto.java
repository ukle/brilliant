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
public class SysUserForUpdateDto {

    @Schema(description = "主键Id")
    private Long id;

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

    @Schema(description = "状态（1启用，0禁用）")
    private Integer status;

    @Schema(description = "绑定角色")
    private List<Long> roleIds;

    public void mergeTo(SysUser entity) {
        entity.setStatus(this.getStatus());
        entity.setUsername(this.getUsername());
        entity.setPhone(this.getPhone());
        entity.setName(this.getName());
    }

}
