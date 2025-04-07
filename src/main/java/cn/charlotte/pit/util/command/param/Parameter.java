package cn.charlotte.pit.util.command.param;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Parameter {

    String name();

    boolean wildcard() default (false);

    String defaultValue() default ("");

    String[] tabCompleteFlags() default ("");

}