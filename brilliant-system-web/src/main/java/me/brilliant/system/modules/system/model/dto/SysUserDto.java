package me.brilliant.system.modules.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/10
 */
@Data
public class SysUserDto {

    @Schema(description="用户ID")
    private Long id;

    @Schema(description="用户名")
    private String username;

    @Schema(description="昵称")
    private String nickname;

    @Schema(description="手机号码")
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
    private List<Long> roleIds;

}
