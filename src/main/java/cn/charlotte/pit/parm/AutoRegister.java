package cn.charlotte.pit.parm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/12 22:39
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoRegister {
    boolean shouldRegister = true;
}
