package me.brilliant.system.modules.system.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import me.brilliant.system.common.BasePageQuery;

import java.time.LocalDate;

/**
 * @author Star Chou
 * @description /
 * @create 2024/8/23
 */
@Schema(description ="用户分页查询对象")
@Data
public class UserPageQuery extends BasePageQuery {

    @Schema(description="关键字(用户名/昵称/手机号)")
    private String keywords;

    @Schema(description="用户状态")
    private Integer status;

    @Schema(description="部门ID")
    private Long deptId;

    @Schema(description="创建时间-开始时间")
    private LocalDate startTime;

    @Schema(description="创建时间-结束时间")
    private LocalDate endTime;
}
