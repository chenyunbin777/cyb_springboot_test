package com.cyb.codetest.reflect;

import lombok.Data;

/**
 * @author cyb
 * @date 2021/1/3 1:41 下午
 */
@Data
@AnnotationTest(value = "cyb" ,key = "name")
public class Ref {
    private String name;
}
