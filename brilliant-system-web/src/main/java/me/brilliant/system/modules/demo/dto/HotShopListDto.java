package me.brilliant.system.modules.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/25
 */
@Data
@Builder
public class HotShopListDto {

    @Schema(description = "主键Id")
    private String id;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "图片url")
    private String imgUrl;
    @Schema(description = "销量")
    private int sales;
    @Schema(description = "")
    private int expressLimit;
    @Schema(description = "起送价格")
    private int expressPrice;
    @Schema(description = "宣传语")
    private String slogan;
}
