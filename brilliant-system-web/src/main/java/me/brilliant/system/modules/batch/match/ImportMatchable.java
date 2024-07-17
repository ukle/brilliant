package me.brilliant.system.modules.batch.match;

import me.brilliant.system.modules.batch.ImportProviderContext;
import me.brilliant.system.modules.batch.dto.ImportMatchResult;

import java.util.List;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/17
 */
public interface ImportMatchable {

    List<ImportMatchResult> match(ImportProviderContext context, int num);

}
