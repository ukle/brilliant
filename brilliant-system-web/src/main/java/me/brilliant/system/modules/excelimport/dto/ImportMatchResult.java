package me.brilliant.system.modules.excelimport.dto;

import lombok.Data;
import me.brilliant.boot.web.exception.ResultException;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/17
 */
@Data
public class ImportMatchResult {

    private String code;
    private String msg;
    private String num;

    public ImportMatchResult() {
    }

    public ImportMatchResult(String str,int num) {
        this.code = "500";
        this.msg = str;
        this.num = "第"+num+"条数据错误";
    }

    public boolean success() {
        return code.equals("0");
    }

    public ResultException toException() {
        return new ResultException(code, msg);
    }

    public ResultException toException(String msg) {
        return new ResultException("500", msg);
    }

    public void setMsg(String msg) {
        this.code = "500";
        this.msg = msg;
    }
}

