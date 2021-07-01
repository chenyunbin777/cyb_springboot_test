package com.cyb.codetest.JVM;

/**
 * @author cyb
 * @date 2020/12/30 3:58 下午
 */
public class StaticBlockTest {

    public static void main(String[] args) {
        new Child();
    }
}

class Child extends Parents {
    public Child() {
        System.out.println("子类构造方法");
    }

    {
        System.out.println("子类代码块");
    }

    static {
        System.out.println("子类静态代码块");
    }


}

class Parents {
    public Parents() {
        System.out.println("父类构造方法");
    }

    {
        System.out.println("父类代码块");
    }

    static {
        System.out.println("父类静态代码块");
    }

    public static void find() {
        System.out.println("静态方法");
    }
}