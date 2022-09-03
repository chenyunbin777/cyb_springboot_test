package com.cyb.codetest.算法.第五章_高频面试题;

/**
 * @author cyb
 * @date 2022/9/1 下午10:49
 */
public class 删除有序数组中的重复项_26 {


    /**
     * 快慢指针解决
     * @param nums
     * @return
     */
    public int removeDuplicates(int[] nums) {
        if(nums.length == 0){
            return 0;
        }

        int slow = 0, fast = 1;

        //不相等 移动fast到slow+1
        while (fast < nums.length) {
            if (nums[slow] != nums[fast]) {
                slow++;
                nums[slow] = nums[fast];
            }
            fast++;

        }

        return slow + 1;

    }
}
