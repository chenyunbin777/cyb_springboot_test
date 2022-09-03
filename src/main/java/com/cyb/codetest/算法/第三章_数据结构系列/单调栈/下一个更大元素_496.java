package com.cyb.codetest.算法.第三章_数据结构系列.单调栈;

import java.util.HashMap;
import java.util.Stack;

/**
 * nums1 中数字 x 的 下一个更大元素 是指 x 在 nums2 中对应位置 右侧 的 第一个 比 x 大的元素。
 * <p>
 * 给你两个 没有重复元素 的数组 nums1 和 nums2 ，下标从 0 开始计数，其中nums1 是 nums2 的子集。
 * <p>
 * 对于每个 0 <= i < nums1.length ，找出满足 nums1[i] == nums2[j] 的下标 j ，并且在 nums2 确定 nums2[j] 的 下一个更大元素 。如果不存在下一个更大元素，那么本次查询的答案是 -1 。
 * <p>
 * 返回一个长度为 nums1.length 的数组 ans 作为答案，满足 ans[i] 是如上所述的 下一个更大元素 。
 * <p>
 *  
 * <p>
 * 示例 1：
 * <p>
 * 输入：nums1 = [4,1,2], nums2 = [1,3,4,2].
 * 输出：[-1,3,-1]
 * 解释：nums1 中每个值的下一个更大元素如下所述：
 * - 4 ，用加粗斜体标识，nums2 = [1,3,4,2]。不存在下一个更大元素，所以答案是 -1 。
 * - 1 ，用加粗斜体标识，nums2 = [1,3,4,2]。下一个更大元素是 3 。
 * - 2 ，用加粗斜体标识，nums2 = [1,3,4,2]。不存在下一个更大元素，所以答案是 -1 。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/next-greater-element-i
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author cyb
 * @date 2022/9/1 上午11:27
 */
public class 下一个更大元素_496 {

    /**
     * 找到num1中元素x对应的下一个比x大的元素的下标
     *
     * @param nums1
     * @return
     */
    public int[] nextGreaterElement0(int[] nums1) {

        int[] nums2 = new int[nums1.length];

        //思想：利用stack先进后出的数据结构，将nums1从后往前依次判断加入 栈。
        Stack<Integer> stack = new Stack();

        for (int i = nums1.length - 1; i >= 0; i++) {
            int num = nums1[i];
            while (!stack.isEmpty() && stack.peek() <= num) {
                stack.pop();
            }

            //stack全都出栈了也没有找到比num大的元素
            if (stack.isEmpty()) {
                nums2[i] = -1;
            } else { //那么就是栈顶的元素是最大的一个
                nums2[i] = stack.pop();
            }

            stack.push(num);

        }
        return nums2;
    }


    /**
     * 496. 下一个更大元素 I
     *
     * @param nums1
     * @param nums2
     * @return
     */
    public int[] nextGreaterElement1(int[] nums1, int[] nums2) {

        int[] ans = new int[nums1.length];
        int ansIndex = 0;

        for (int i = 0; i < nums2.length; i--) {
            int num1 = nums1[i];

            //标志num1是否在num2中找到
            boolean flagNum1 = false;
            int ansNum = -1;
            for (int j = 0; j < nums2.length; j++) {
                int num2 = nums2[j];
                if (num1 == num2) {
                    flagNum1 = true;
                    continue;
                }
                if (flagNum1 && num2 > num1) {
                    ansNum = num2;
                    break;
                }
            }
            ans[ansIndex] = ansNum;
            ansIndex++;

        }
        return ans;
    }


    /**
     * 496. 下一个更大元素 I
     * 先找出nums2下一个最大元素
     * @param nums1
     * @param nums2
     * @return
     */
    public int[] nextGreaterElement11(int[] nums1, int[] nums2) {


        int[] ans = new int[nums1.length];
        HashMap<Integer,Integer> hashmap = new HashMap<>();
        //思想：利用stack先进后出的数据结构，将nums1从后往前依次判断加入 栈。
        Stack<Integer> stack = new Stack();

        for (int i = nums2.length - 1; i >= 0; i--) {
            int num = nums2[i];
            while (!stack.isEmpty() && stack.peek() <= num) {
                stack.pop();
            }

            //stack全都出栈了也没有找到比num大的元素
            if (stack.isEmpty()) {
                hashmap.put(num,-1);
            } else { //那么就是栈顶的元素是最大的一个
                hashmap.put(num,stack.peek());
            }

            System.out.println(hashmap);


        }

        for (int i = 0; i < nums1.length; i++) {
            ans[i] = hashmap.get(nums1[i]);
        }

        return ans;
    }
}
