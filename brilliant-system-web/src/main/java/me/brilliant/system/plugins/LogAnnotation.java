package me.brilliant.system.plugins;

import java.lang.annotation.*;

/**
 * @author Star Chou
 * @description /
 * @create 2024/8/23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface LogAnnotation {

    String value() default "";

    LogModuleEnum module()  ;

}
