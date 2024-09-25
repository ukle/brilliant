package me.brilliant.system.modules.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author Star Chou
 * @description 用户分页视图对象
 * @create 2024/8/23
 */
@Schema(description ="用户分页对象")
@Data
public class UserPageVO {

    @Schema(description="用户ID")
    private Long id;

    @Schema(description="用户名")
    private String username;

    @Schema(description="用户昵称")
    private String nickname;

    @Schema(description="手机号")
    private String mobile;

    @Schema(description="性别")
    private String genderLabel;

    @Schema(description="用户头像地址")
    private String avatar;

    @Schema(description="用户邮箱")
    private String email;

    @Schema(description="用户状态(1:启用;0:禁用)")
    private Integer status;

    @Schema(description="部门名称")
    private String deptName;

    @Schema(description="角色名称，多个使用英文逗号(,)分割")
    private String roleNames;

    @Schema(description="创建时间")
    private LocalDate createTime;

}
