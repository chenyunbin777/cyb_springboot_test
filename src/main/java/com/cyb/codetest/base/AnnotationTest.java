package com.cyb.codetest.base;

import java.lang.annotation.*;

/**
 * @author cyb
 * @date 2021/1/19 4:04 下午
 */

@Target(ElementType.ANNOTATION_TYPE)
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationTest {
}
