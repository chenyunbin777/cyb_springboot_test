package com.cyb.codetest.jdk8.方法引用;

import com.cyb.codetest.base.InterfaceTest;

import java.util.Arrays;
import java.util.List;

/**
 * @author cyb
 * @date 2022/9/12 上午12:01
 */
public class Car {

    //Supplier是jdk1.8的接口，这里和lamda一起使用了
    public static Car create(final Supplier<Car> supplier) {
        return supplier.get();
    }

    public static void collide(final Car car) {
        System.out.println("Collided " + car.toString());
    }

    public void follow(final Car another) {
        System.out.println("Following the " + another.toString());
    }

    public void repair() {
        System.out.println("Repaired " + this.toString());
    }


    public static void main(String[] args) {
        
        // 1 构造器引用：它的语法是Class::new，或者更一般的Class< T >::new实例如下：
        final Car car = Car.create( Car::new );


        final List< Car > cars = Arrays.asList( car );
    }
}
