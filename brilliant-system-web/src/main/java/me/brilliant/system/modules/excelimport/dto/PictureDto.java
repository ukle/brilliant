package me.brilliant.system.modules.excelimport.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/17
 */
@Data
public class PictureDto implements Serializable {

    @Schema(description = "顺序")
    private Integer picSorts;

    @Schema(description = "图片组别")
    private String picGroup;

    @Schema(description = "图片路径")
    private String picPath;

    @Schema(description = "路径")
    private String picUrl;

    @Schema(description = "base64")
    private String base64;
}
