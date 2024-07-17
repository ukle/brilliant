package me.brilliant.system.modules.excelimport.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import me.brilliant.system.modules.excelimport.entity.AlumnusArchive;

import java.util.List;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/17
 */
@Data
public class ValidResultDto {

    private boolean validSuccess = true;

    private int totalCount;

    private List<ImportMatchResult> matchResults;

    @JsonIgnore
    private List<AlumnusArchive> alumnusArchives;
}
