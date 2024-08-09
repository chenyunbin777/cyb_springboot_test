package com.cyb.codetest.多线程.forkJoin;

/**
 * @auther cyb
 * @date 2024/1/27 15:02
 */
public class StatusTest {

    public static void main(String[] args) {
         int status; // accessed directly by pool and workers
         final int DONE_MASK   = 0xf0000000;  // mask out non-completion bits
         final int NORMAL      = 0xf0000000;  // must be negative
         final int CANCELLED   = 0xc0000000;  // must be < NORMAL
         final int EXCEPTIONAL = 0x80000000;  // must be < CANCELLED
         final int SIGNAL      = 0x00010000;  // must be >= 1 << 16
         final int SMASK       = 0x0000ffff;  // short bits for tags

        int i = -1 & DONE_MASK;

        int num = -1; // 要转换的整数
        String hexNum = Integer.toHexString(num); // 调用toHexString()方法将整数转换为十六进制字符串
        System.out.println("整数 " + num + " 对应的十六进制值为：" + hexNum);

        System.out.println(i);

        if ((-1 & DONE_MASK) != NORMAL){
            System.out.println(111);
        }else {
            System.out.println(222);
        }
    }
}
