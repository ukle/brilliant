package me.brilliant.system.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Star Chou
 * @description 基础分页请求对象
 * @create 2024/8/23
 */
@Data
@Schema(description ="基础分页请求对象")
public class BasePageQuery {

    @Schema(description = "页码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private int pageNum = 1;

    @Schema(description = "每页记录数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private int pageSize = 10;
}
