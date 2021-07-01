package com.cyb.codetest.算法;

import com.alibaba.fastjson.JSON;

/**
 * @author cyb
 * @date 2021/1/11 10:42 下午
 */
public class TestKuaishou {

    public static void main(String[] args) {
//        int[] arr = {-1, 4, 2, 3};
        int[] arr = {2, 6, 1, 3, 3, 5, 4};
//        int[] arr = {2, -6, -1, 3, 1, 3, 9, 5, 4};
        int minInteger = getMinInteger(arr);
        System.out.println(minInteger);
        int x = 0b001;
//        System.out.println(x);
    }

    //给定一个无序数组，找出不存在于数组中的最小正整数的数据

    /**
     * * 如果1没有出现 那么最小结果为1
     * <p>
     * * 如果1到n都出现那么最下的结果为n+1
     * <p>
     * * 因此结果的范围1～n+1
     *
     * @param arr
     * @return
     */
    public static int getMinInteger(int[] arr) {
        System.out.println("arr:" + JSON.toJSONString(arr));
        System.out.println("arr.length:" + arr.length);
        int result = Integer.MAX_VALUE;

        int flag = Integer.MAX_VALUE;
        int arrNum = 0, temp = 0;
        //如果有小于0的数，说明  1~arr.length的最大数之间一定存在一个result。
        //有数超出了 1~arr.length的范围，说明  1~arr.length的最大数之间一定存在一个result。
        for (int i = 0; i < arr.length; i++) {
            arrNum = arr[i];
            if (arrNum < 0 || arrNum > arr.length) {
                flag = 1;
                arr[i] = 0;
            }
        }
        System.out.println("arr<0 大禹长度都置为0:" + JSON.toJSONString(arr));

        for (int i = 0; i < arr.length; i++) {
            arrNum = arr[i];
            //当前位置就是i+1
            if (arrNum == i + 1) {
                continue;
            }

            while (arrNum != 0 && arrNum != i + 1) {

                if (arr[arrNum - 1] == arrNum) {
                    break;
                }

                temp = arr[arrNum - 1];
                arr[arrNum - 1] = arrNum;
                arr[i] = temp;
                arrNum = temp;

            }

            System.out.println("arr交换完成:" + JSON.toJSONString(arr));

        }
        System.out.println("arr交换完成:" + JSON.toJSONString(arr));
        //说明arr 是1~arr.length排列的
        if (flag == 0) {
            return arr.length + 1;
        }

        for (int i = 0; i < arr.length; i++) {
            arrNum = arr[i];
            if (arrNum == 0 || arrNum != i + 1) {
                result = i + 1;
                break;
            }
        }

        return result;


    }


}


