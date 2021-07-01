package com.cyb.codetest.设计模式.构建器模式_构造方法模式;

/**
 * 对Builer模式使用方法的总结：
 * （1）、外部类的构造函数私有，且参数为静态内部类；
 * （2）、静态内部类拥有外部类相同的属；
 * （3）、为每一个属性，写一个方法，返回的是Builer；
 * （4）、最后一个方法是build方法，用于构建一个外部类；
 *
 * @author cyb
 * @date 2021/2/16 10:43 上午
 */
public class Person {
    //必要参数
    private final int id;
    private final String name;
    //可选参数
    private int age;
    private String sex;
    private String phone;
    private String address;
    private String desc;

    /**
     * 私有化构造方法
     *
     * @param builder
     */
    private Person(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.age = builder.age;
        this.sex = builder.sex;
        this.phone = builder.phone;
        this.address = builder.address;
        this.desc = builder.desc;
    }

    /**
     * 静态内部类
     */
    public static class Builder {
        //必要参数
        private final int id;
        private final String name;
        //可选参数
        private int age;
        private String sex;
        private String phone;
        private String address;
        private String desc;

        public Builder(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public Builder age(int val) {
            this.age = val;
            return this;
        }

        public Builder sex(String val) {
            this.sex = val;
            return this;
        }

        public Builder phone(String val) {
            this.phone = val;
            return this;
        }

        public Builder address(String val) {
            this.address = val;
            return this;
        }

        public Builder desc(String val) {
            this.desc = val;
            return this;
        }

        public Person build() {
            return new Person(this);
        }
    }

}
