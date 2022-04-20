package com.cyb.codetest.算法.排序;

import com.alibaba.fastjson.JSON;

/**
 * @author cyb
 * @date 2022/4/7 下午4:19
 */
public class GuiBingSort {

    public static void main(String[] args) {
        int[] arr = {10, 7, 1, 4, 11, 8, 3, 2, 9};
        GuiBingSort(arr);
        System.out.println(JSON.toJSONString(arr));
    }

    public static void GuiBingSort(int[] arr) {

        GuiBingSortChild(arr, 0, arr.length - 1);

    }


    public static void GuiBingSortChild(int[] arr, int start, int end) {

        //一个元素就不用排序了
        if (end - start <= 0) {
            return;
        }

        int mid = (end + start) / 2;
//        System.out.println("start==="+start);
//        System.out.println("mid==="+mid);
//        System.out.println("end==="+end);

        //结果A,排序逻辑哪里做？？？ 这里就是我的思维误区了，正常的所有的合并操作都是在GuiBingSortReal中实现的。递归的时候会分为两个步骤：
        // 1:递，也就是把逻辑进入最内层去执行，然后一点一点的向外扩散开来
        // 2:归，也就是归纳，将每一个子逻辑归纳成为一个总的结果。
        GuiBingSortChild(arr, start, mid);
        //结果B
        GuiBingSortChild(arr, mid + 1, end);
        //合并 A B序列  这是一个单独的方法
        GuiBingSortReal(arr, start, mid, end);
    }

    /**
     * 1 3 5 / 2 3 6   这两个子序列合并
     * 前半段包含mid，后半段不包含mid
     *
     * @param arr
     * @param start
     * @param mid
     * @param end
     */
    private static void GuiBingSortReal(int[] arr, int start, int mid, int end) {

        //1 将start到end的所有数据放到一个临时数组中
        int[] tmp = new int[end - start + 1];

        //2 将两个序列头部开始比较，较小的元素放到tmp中并且向后移动1位，另外一个序列不做然后操作。
        int tmpIndex = 0;
        int i = start, j = mid + 1;
        for (; i <= mid && j <= end; ) {
            //这里<=保证了归并排序的稳定性
            if (arr[i] <= arr[j]) {
                tmp[tmpIndex] = arr[i];
                i++;
            } else {
                tmp[tmpIndex] = arr[j];
                j++;
            }
            tmpIndex++;
        }

        //3 看看那个区间还有剩余的数据直接放在tmp末尾。
        for (; i <= mid; i++) {

            tmp[tmpIndex] = arr[i];
            tmpIndex++;
        }
        for (; j <= end; j++) {

            tmp[tmpIndex] = arr[j];
            tmpIndex++;
        }

        tmpIndex = 0;

        //4 将tmp数据copy回arr
        for (int k = start; k <= end; k++) {

            arr[k] = tmp[tmpIndex];
            tmpIndex++;
        }

    }

}
