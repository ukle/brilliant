package me.brilliant.system.modules.excelimport.match;

import me.brilliant.system.modules.excelimport.ImportProviderContext;
import me.brilliant.system.modules.excelimport.dto.ImportMatchResult;

import java.util.List;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/17
 */
public interface ImportMatchable {

    List<ImportMatchResult> match(ImportProviderContext context, int num);

}
