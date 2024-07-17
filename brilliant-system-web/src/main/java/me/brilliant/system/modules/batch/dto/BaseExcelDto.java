package me.brilliant.system.modules.batch.dto;

import lombok.Data;
import me.brilliant.system.modules.batch.entity.AlumnusArchive;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/17
 */
@Data
public class BaseExcelDto {

    private String failReason;

    private int index;

    public AlumnusArchive toAlumnusArchive() {
        AlumnusArchive alumnusArchive = new AlumnusArchive();
        return alumnusArchive;
    }
}
