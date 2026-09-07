package com.cyb.codetest.算法.排序;

import java.util.Arrays;

/**
 * 在这个实现中，我们首先构建堆，然后不断地移除最大元素并重新构建堆，
 * 直到堆中只剩下一个元素。这样就能得到一个排序好的数组。
 * @author cyb
 * @date 2024/9/12 上午11:21
 */
public class HeapSort {

    public void sort(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }

        // 1. Build heap
        for (int i = arr.length / 2 - 1; i >= 0; i--) {
            heapify(arr, arr.length, i);
        }

        // 2. Heap sort
        for (int i = arr.length - 1; i > 0; i--) {
            // Swap the root with the last element
            swap(arr, 0, i);
            // Heapify the remaining elements
            heapify(arr, i, 0);
        }
    }

    private void heapify(int[] arr, int heapSize, int index) {
        int left = index * 2 + 1;
        int right = index * 2 + 2;
        int largest = index;

        if (left < heapSize && arr[left] > arr[largest]) {
            largest = left;
        }

        if (right < heapSize && arr[right] > arr[largest]) {
            largest = right;
        }

        if (largest != index) {
            swap(arr, index, largest);
            heapify(arr, heapSize, largest);
        }
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        HeapSort heapSort = new HeapSort();
        int[] arr = {10, 2, 3, 4, 5, 6, 7, 8, 9};
        heapSort.sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
