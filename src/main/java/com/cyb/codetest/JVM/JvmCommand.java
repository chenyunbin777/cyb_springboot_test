package com.cyb.codetest.JVM;

/**
 * @author cyb
 * @date 2021/2/24 5:37 下午
 */
public class JvmCommand {
    public static void main(String[] args) throws InterruptedException {
        while (true) {
            Thread.sleep(1000);
            System.out.println(123);
        }
    }
}
