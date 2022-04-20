package com.cyb.codetest.算法.排序;

/**
 * @author cyb
 * @date 2022/4/7 下午3:03
 */
public class InsertionSort {


    // 插入排序，a表示数组，n表示数组大小
    public static void insertionSort(int[] a) {
        int n = a.length;
        if (n <= 1) return;

        for (int i = 1; i < n; ++i) {
            int value = a[i];
            int j = i - 1;
            // 查找插入的位置
            // 从前边有序的序列尾开始查找
            for (; j >= 0; --j) {
                if (a[j] > value) {
                    a[j + 1] = a[j];  // 数据移动
                } else {
                    break;
                }
            }
            a[j + 1] = value; // 插入数据
        }
    }
}
