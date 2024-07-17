package me.brilliant.system.modules.excelimport.constant;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/17
 */
public enum ImportType {

    /**
     * 曾在校生
     */
    STUDENT(1,"student"),
    /**
     * 曾在职员工
     */
    STAFF(2,"staff"),
    /**
     * 其他
     */
    OTHER(3,"other"),
    ;

    private int code;

    private String value;

    ImportType(int code, String value){
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static ImportType getImportType(int code) {
        for (ImportType value : ImportType.values()) {
            if (code == value.getCode()) {
                return value;
            }
        }
        return null;
    }
}
