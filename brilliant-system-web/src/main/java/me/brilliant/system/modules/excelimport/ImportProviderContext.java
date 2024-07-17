package me.brilliant.system.modules.excelimport;

import me.brilliant.system.modules.excelimport.dto.ImportMatchResult;
import me.brilliant.system.modules.excelimport.match.ImportMatchable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/17
 */
public class ImportProviderContext {


    private HashMap<String, Object> attributes = new HashMap<>();

    public Object getAttributes(String key) {
        return attributes.get(key);
    }

    public void setAttributes(String key, Object object) {
        attributes.put(key, object);
    }

    List<ImportMatchResult> matches(List<ImportMatchable> handlers, int num) {
        List<ImportMatchResult> list = new ArrayList<>();
        if (handlers != null) {
            for (ImportMatchable handler : handlers) {
                List<ImportMatchResult> match = handler.match(this,num);
                list.addAll(match);
            }
        }
        return list;
    }
}
