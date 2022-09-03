package com.cyb.codetest.算法.第五章_高频面试题.贪心算法;

/**
 * @author cyb
 * @date 2022/9/2 上午12:12
 */
public class 跳跃游戏 {

    public boolean canJump(int[] nums) {
        int fast = 0;

        for (int i = 0; i < nums.length; i++) {
            //说明i是可达位置
            if (i <= fast) {
                //计算在当前位置i的最远可达位置
                fast = Math.max(fast, i+nums[i]);

                //如果遍历到最后 最大移动距离>=最后一个位置 说明可以移动到
                if(fast >= nums.length-1){
                    return true;
                }
            }
        }
        return false;
    }
}
