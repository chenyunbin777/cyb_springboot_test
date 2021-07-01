package com.cyb.codetest.base;

/**
 * @author cyb
 * @date 2021/2/20 9:58 上午
 */
public class StringTest {

    public static void main(String[] args) {

        String str = "123";
        String intern = str.intern();
        System.out.println(intern.equals(str));


        String str2 = new String("123");


        System.out.println(str2.equals(str));

        System.out.println(str == str2);
    }

}
