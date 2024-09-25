package me.brilliant.system.modules.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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

    @Schema(description="用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description="昵称")
    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @Schema(description="手机号码")
    @Pattern(regexp = "^$|^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$", message = "手机号码格式不正确")
    private String mobile;

    @Schema(description="性别")
    private Integer gender;

    @Schema(description="用户头像")
    private String avatar;

    @Schema(description="邮箱")
    private String email;

    @Schema(description="用户状态(1:正常;0:禁用)")
    private Integer status;

    @Schema(description="部门ID")
    private Long deptId;

    @Schema(description="角色ID集合")
    @NotEmpty(message = "用户角色不能为空")
    private List<Long> roleIds;

    public void mergeTo(SysUser sysUser) {
        sysUser.setStatus(this.getStatus());
        sysUser.setEmail(this.getEmail());
        // sysUser.setCreateTime(null);
        // sysUser.setCreateBy(null);
        sysUser.setGender(this.getGender());
        sysUser.setDeptId(this.getDeptId());
        sysUser.setMobile(this.getMobile());
        sysUser.setAvatar(this.getAvatar());
        // sysUser.setUpdateBy(null);
        sysUser.setUsername(this.getUsername());
        // sysUser.setUpdateTime(null);
        sysUser.setId(this.getId());
        // sysUser.setDeleted(null);
        sysUser.setNickname(this.getNickname());
        // sysUser.setPassword(null);
    }
}
