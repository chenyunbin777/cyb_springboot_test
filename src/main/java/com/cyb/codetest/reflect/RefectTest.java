package com.cyb.codetest.reflect;

import com.alibaba.fastjson.JSON;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 取消private的封装效果
 *
 * @author cyb
 * @date 2021/1/3 1:39 下午
 */
public class RefectTest {

    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("com.cyb.codetest.reflect.Ref");
        Ref obj = (Ref) clazz.newInstance();
        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);// 设置取消private String name;的封装效果 可以直接获取name属性
        nameField.set(obj, "Feild 测试");
        System.out.println(nameField.get(obj));

        //com.cyb.codetest.reflect.AnnotationTest
        Class<Annotation> aClass = (Class<Annotation>) Class.forName("com.cyb.codetest.reflect.AnnotationTest");
        Annotation annotation = clazz.getAnnotation(aClass);
        System.out.println("annotation:" + JSON.toJSONString(annotation));

    }


}
