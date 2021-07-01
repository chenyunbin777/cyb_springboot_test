package com.cyb.codetest.设计模式.构建器模式_构造方法模式;

/**
 * @author cyb
 * @date 2021/2/16 10:45 上午
 */
public class TestMain {

    public static void main(String[] args) {
        Person person = new Person.Builder(1, "张小毛")
                .age(22).sex("男").desc("使用builder模式").build();
        System.out.println(person.toString());

        Person.Builder builder = new Person.Builder(1, "张小毛");
    }
}
