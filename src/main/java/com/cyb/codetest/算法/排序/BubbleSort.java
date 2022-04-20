package com.cyb.codetest.算法.排序;

import com.alibaba.fastjson.JSON;

/**
 * @author cyb
 * @date 2022/4/7 下午2:24
 */
public class BubbleSort {

    public static void main(String[] args) {
        int[] arr = {10, 7, 1, 4, 8, 3, 2, 9};
        BubbleSort(arr);
        System.out.println(JSON.toJSONString(arr));
    }

    //我写的
    public static void BubbleSort(int[] arr) {
        if (arr.length <= 0) {
            return;
        }
        for (int i = 0; i < arr.length - 1; i++) {
            boolean flag = false;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {

                    int tmp = arr[j];
                    arr[j] = arr[i];
                    arr[i] = tmp;
                    flag = true;

                }
            }
            if (!flag) {
                break;
            }
        }

    }


    // 冒泡排序，a表示数组，n表示数组大小
    public void bubbleSort(int[] a, int n) {
        if (n <= 1) return;

        for (int i = 0; i < n; ++i) {
            // 提前退出冒泡循环的标志位
            boolean flag = false;
            for (int j = 0; j < n - i - 1; ++j) {
                if (a[j] > a[j + 1]) { // 交换
                    int tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                    flag = true;  // 表示有数据交换
                }
            }
            if (!flag) break;  // 没有数据交换，提前退出
        }
    }
}
