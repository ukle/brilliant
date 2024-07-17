package me.brilliant.system.modules.batch.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import me.brilliant.system.modules.batch.entity.AlumnusArchive;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/17
 */
public class OtherExcelDto extends BaseExcelDto {

    @ExcelProperty(index = 0)
    @Schema(description = "姓名")
    private String name;

    @ExcelProperty(index = 1)
    @Schema(description = "身份证号码")
    private String idNumber;

    @ExcelProperty(index = 2)
    @Schema(description = "学历")
    private String eduBackgroundStr;

    @ExcelProperty(index = 3)
    @Schema(description = "性别")
    private String sexStr;

    @ExcelProperty(index = 4)
    @Schema(description = "培训时间")
    private String trainTime;

    @ExcelProperty(index = 5)
    @Schema(description = "培训名称")
    private String trainName;

    @ExcelProperty(index = 6)
    @Schema(description = "证明人")
    private String witness;

    @ExcelProperty(index = 7)
    @Schema(description = "主办单位")
    private String trainCompany;

    @ExcelProperty(index = 8)
    @Schema(description = "民族")
    private String nationStr;

    @ExcelProperty(index = 9)
    @Schema(description = "籍贯")
    private String origin;

    @ExcelProperty(index = 10)
    @Schema(description = "政治面貌")
    private String politicsStatusStr;

    @ExcelProperty(index = 11)
    @Schema(description = "地址")
    private String address;

    @ExcelProperty(index = 12)
    @Schema(description = "手机号码")
    private String phone;

    @ExcelProperty(index = 13)
    @Schema(description = "邮箱")
    private String email;

    private Integer eduBackground;

    private Integer sex;

    private String nation;

    private String politicsStatus;

    /**
     * 判断导入的excel是不是空行
     */
    public boolean isEmptyLine() {
        // StringUtils.isEmpty(this.name) && StringUtils.isEmpty(idNumber)

        return false;
    }

    @Override
    public AlumnusArchive toAlumnusArchive() {
        AlumnusArchive alumnusArchive = new AlumnusArchive();
        // todo
        return alumnusArchive;
    }
}
