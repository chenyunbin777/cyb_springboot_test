package com.cyb.codetest.reflect;

import lombok.Data;

import java.util.ArrayList;

/**
 * @author cyb
 * @date 2021/1/3 1:41 下午
 */
@Data
@AnnotationTest(value = "cyb", key = "name")
public class Ref {
    private String name;

    public void refMethod(String a,int b){
        System.out.println("a:"+a+" b:"+b);
    }

    public void method1(String a,Integer b){}

    public void method(){
        System.out.println("method");
    }

    public ArrayList<String> method2(){
        System.out.println("泛型类返回测试");

        return new ArrayList<>();
    }

}
