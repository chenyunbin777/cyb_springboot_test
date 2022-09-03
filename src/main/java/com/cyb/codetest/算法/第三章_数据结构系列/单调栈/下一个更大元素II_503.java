package com.cyb.codetest.算法.第三章_数据结构系列.单调栈;

import java.util.Stack;

/**
 * 给定一个循环数组 nums （ nums[nums.length - 1] 的下一个元素是 nums[0] ），返回 nums 中每个元素的 下一个更大元素 。
 * <p>
 * 数字 x 的 下一个更大的元素 是按数组遍历顺序，这个数字之后的第一个比它更大的数，这意味着你应该循环地搜索它的下一个更大的数。如果不存在，则输出 -1 。
 * <p>
 *  
 * <p>
 * 示例 1:
 * <p>
 * 输入: nums = [1,2,1]
 * 输出: [2,-1,2]
 * 解释: 第一个 1 的下一个更大的数是 2；
 * 数字 2 找不到下一个更大的数；
 * 第二个 1 的下一个最大的数需要循环搜索，结果也是 2。
 * 示例 2:
 * <p>
 * 输入: nums = [1,2,3,4,3]
 * 输出: [2,3,4,-1,4]
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/next-greater-element-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author cyb
 * @date 2022/9/1 下午3:08
 */
public class 下一个更大元素II_503 {


    public int[] nextGreaterElements(int[] nums) {
        int length = nums.length;
        int[] ans = new int[length];


        Stack<Integer> stack = new Stack();

        for (int i = 2 * length - 1; i >= 0; i--) {

            //防止数组越界 使用%来实现循环数组
            int num = nums[i % length];

            while (!stack.isEmpty() && stack.peek() <= num) {

                stack.pop();
            }


            int nextGreaterElement = stack.isEmpty() ? -1 : stack.peek();
            ans[i % length] = nextGreaterElement;

            stack.push(num);
        }

        return ans;


    }
}
