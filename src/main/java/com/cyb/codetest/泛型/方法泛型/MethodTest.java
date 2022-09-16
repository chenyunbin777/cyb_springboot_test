package com.cyb.codetest.泛型.方法泛型;

/**
 * @author cyb
 * @date 2022/9/12 下午1:32
 */
public class MethodTest {


    /**
     * <T> 这个代表我方法中需要的使用到的值的类型，如果返回值用到了也可以使用T来代替
     * @param value
     * @param <T>
     * @return
     */
    public static <T> T get(T value){
        System.out.println("value:"+value);
        return value;
    }





    public static void main(String[] args) {
        Integer integer = MethodTest.get(1);

        System.out.println("integer:"+integer);


        String str = MethodTest.get("123");
        System.out.println("str："+str);
    }
}
