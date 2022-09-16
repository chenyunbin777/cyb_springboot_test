package com.cyb.codetest.jdk8.方法引用;

/**
 *
 * 函数式接口(Functional Interface)就是一个有且仅有一个抽象方法，但是可以有多个非抽象方法的接口。
 *
 * 函数式接口可以被隐式转换为 lambda 表达式。
 *
 * Lambda 表达式和方法引用（实际上也可认为是Lambda表达式）上。
 * @author cyb
 * @date 2022/9/11 下午11:58
 */
@FunctionalInterface
public interface Supplier<T> {
    T get();



}
