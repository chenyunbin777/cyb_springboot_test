package com.cyb.codetest.reflect;

import com.alibaba.fastjson.JSON;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

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


        //方法
        Method refMethod = clazz.getMethod("refMethod", String.class, int.class);
//        Method refMethod = clazz.getMethod("method");
        //获取方法的类型 public等
        int modifiers = refMethod.getModifiers();
        Class<?> returnType = refMethod.getReturnType();
        String name = refMethod.getName();

        Class<?>[] parameterTypes = refMethod.getParameterTypes();

        Parameter[] parameters = refMethod.getParameters();


        System.out.println("modifiers:" + modifiers);

        System.out.println("name:" + name);
        System.out.println("parameters:" + JSON.toJSONString(parameters));


        //获取返回值泛型信息
        Method refMethod2 = clazz.getMethod("method2");
        Type genericReturnType = refMethod2.getGenericReturnType();
        ParameterizedTypeImpl parameterizedType = (ParameterizedTypeImpl)genericReturnType;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Type actualTypeArgument = actualTypeArguments[0];

        //获取返回值泛型类
        Class realPatternType = (Class) actualTypeArgument;
        //返回方法method2 的返回值ArrayList<String> 的泛型类 String
        //realPatternType：class java.lang.String
        System.out.println("realPatternType："+realPatternType);

        //通过反射出来的对象obj来调用方法method2
        refMethod2.invoke(obj);

        //参数 反射调得出的对象 + 参数
        refMethod.invoke(obj,"1",2);
    }


}
