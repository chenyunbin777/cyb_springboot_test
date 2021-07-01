package com.cyb.codetest.reflect;

import java.lang.annotation.*;

/**
 * ElementType.TYPE:修饰"类、接口（包括注释类型）或枚举声明"的注解。
 *
 * @author cyb
 * @Retention(RetentionPolicy.RUNTIME):编译器会将该 Annotation 信息保留在 .class 文件中，并且能被虚拟机读取。
 * Inherited:该注解可以被继承
 * Documented:所标注内容，可以出现在javadoc中。
 * @date 2021/1/3 1:43 下午
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AnnotationTest {
    String value() default "";

    String key() default "";
}
