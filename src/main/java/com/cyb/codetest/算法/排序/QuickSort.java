package com.cyb.codetest.算法.排序;

/**
 * @author cyb
 * @date 2022/4/8 上午10:14
 */
public class QuickSort {


    public static void main(String[] args) {

    }

    /**
     * 取序列的最后一个元素为 分区点
     *
     * 1 2 3 4 5 6 7
     */
    public static void quickSort(int[] arr) {

        quickSortChild(arr,0,arr.length-1, arr.length-1);

    }


    /** 下标 0 1 2 3 4 5 6
     * 例子1：1 2 3 4 5 6 7   分区点下标6，  start = 0  end = 5
     * 1 2 3     4 5 6
     * mid  = (0+5)/2 = 2  作为 1 2 3 的分区点
     * end = 5 作为 4 5 6的分区点
     * @param arr
     * @param start
     * @param end
     * @param pivot 分区点下标
     */
    public static void quickSortChild(int[] arr, int start, int end, int pivot) {

        if(end - start <=0){
            return;
        }

        // (6 + 0) / 2=3
        int mid = (end + start) / 2;

        quickSortChild(arr,start,mid-1,mid);
//        quickSortChild(arr,mid,end);

    }
}
