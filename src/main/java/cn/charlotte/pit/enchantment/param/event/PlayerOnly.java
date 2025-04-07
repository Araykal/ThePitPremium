package cn.charlotte.pit.enchantment.param.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: EmptyIrony
 * @Date: 2020/12/29 23:10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PlayerOnly {
}
