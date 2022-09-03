package com.cyb.codetest.算法.第三章_数据结构系列.单调队列;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author cyb
 * @date 2022/9/1 下午3:31
 */
public class 滑动窗口最大值_239 {


    LinkedList<Integer> window = new LinkedList();


    public void push(int x) {
        while (!window.isEmpty() && window.getLast() < x) {
            window.pollLast();
        }

        window.addLast(x);
    }


    /**
     * 删除滑动窗口中的第一个元素
     *
     * @param x
     */
    public void removeWinFirst(int x) {
        if (x == window.getFirst()) {
            window.pollFirst();
        }

    }

    /**
     * 获取滑动窗口中最大值，也就是队列的队头元素
     */
    public int getWinMax() {
        return window.getFirst();
    }

    public int[] maxSlidingWindow(int[] nums, int k) {


        ArrayList<Integer> ans = new ArrayList();
        for (int i = 0; i < nums.length; i++) {
            if (i  < k - 1) {
                push(nums[i]);
            } else {
                push(nums[i]);
                System.out.println("window:"+ JSON.toJSONString(window));
                System.out.println("getWinMax():"+ getWinMax());
                //添加最大元素到结果
                ans.add(getWinMax());
                //删除滑动列表第一个元素
                removeWinFirst(nums[i - k + 1]);

            }



        }
        int[] ansIntArr = new int[ans.size()];
        for (int j = 0; j < ans.size(); j++) {
            ansIntArr[j] = ans.get(j);
        }

        return ansIntArr;

    }


    public static void main(String[] args) {
        滑动窗口最大值_239 X = new 滑动窗口最大值_239();

        int[] x = {1,3,-1,-3,5,3,6,7};
//        int[] x = {1,-1};
        int k = 3;
        int[] ans = X.maxSlidingWindow(x, k);
        System.out.println(JSON.toJSONString(ans));
    }
}
