package me.brilliant.system.constant;

import me.brilliant.boot.web.result.Result;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/17
 */
public class ErrorCode {

    public static final Result TEMPLATE_TYPE_ERROR = new Result("20020040", "错误的模板类型");
    public static final Result IMPORT_MAX_COUNT_ERROR = new Result("20020100", "最大条数限制10000条");
    public static final Result IMPORT_ZERO_COUNT_ERROR = new Result("20020101", "导入数据不能为空");
    public static final Result EXCEL_ERROR = new Result("20020045", "和模板不同，请下载模板使用");
}
