package me.brilliant.system.modules.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import me.brilliant.boot.web.annotation.Query;


/**
 * @author Star Chou
 * @description /
 * @create 2024/7/10
 */
@Data
public class SysUserQueryCriteria {

    @Query(type = Query.Type.INNER_LIKE)
    @Schema(description = "账号")
    private String username;

    @Query(type = Query.Type.INNER_LIKE)
    @Schema(description = "姓名")
    private String name;

    @Query
    @Schema(description="用户状态")
    private Integer status;

}
