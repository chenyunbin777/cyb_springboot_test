package com.cyb.codetest.base;

/**
 * 函数式接口
 *
 * @author cyb
 * @date 2021/1/8 5:18 下午
 */
@FunctionalInterface
public interface InterfaceTest {
    public static final int a = 0;
    //都是public static final 类型的
    public static final AbstractClassTest abstractClassTest = new AbstractClassTest() {
        @Override
        public void f() {

        }
    };

    void f();

    default void f1() {

    }
}
