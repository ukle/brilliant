package me.brilliant.system.modules.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


/**
 * @author Star Chou
 * @description /
 * @create 2024/7/10
 */
@Data
public class SysUserListDto {

    @Schema(description = "主键Id")
    private Long id;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "角色名集合")
    private List<String> roleNames;

    @Schema(description = "状态（1启用，0禁用）")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
