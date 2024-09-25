package me.brilliant.system.plugins;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * @author Star Chou
 * @description /
 * @create 2024/8/23
 */
@Schema(enumAsRef = true)
@Getter
public enum LogModuleEnum {

    LOGIN("登录"),
    USER("用户"),
    DEPT("部门"),
    ROLE("角色"),
    MENU("菜单"),
    DICT("字典"),
    OTHER("其他")
    ;

    @JsonValue
    private final String moduleName;

    LogModuleEnum(String moduleName) {
        this.moduleName = moduleName;
    }
}
