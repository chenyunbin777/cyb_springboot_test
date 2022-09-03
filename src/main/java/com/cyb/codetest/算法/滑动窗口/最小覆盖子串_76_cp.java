package com.cyb.codetest.算法.滑动窗口;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cyb
 * @date 2022/8/28 下午4:11
 */
public class 最小覆盖子串_76_cp {


    public static void main(String[] args) {

        String minSubString = minWindow("ADOBECODEBANC", "ABC");

        System.out.println(minSubString);
    }


    public static String minWindow(String s, String t) {
        //滑动窗口——左开右闭的区间
        int left = 0, right = 0, valid = 0;
        //返回子串时需要的变量（substring左闭右开）
        int start = 0, end = 0, len = Integer.MAX_VALUE;
        HashMap<Character, Integer> window = new HashMap<>();
        HashMap<Character, Integer> need = new HashMap<>();
        for(int i = 0;i < t.length(); i++){
            char c = t.charAt(i);
            need.put(c,need.getOrDefault(c,0) + 1);
        }
        while(right < s.length()){
            char r = s.charAt(right);
            //扩张右边界
            right++;
            if(need.containsKey(r)){
                window.put(r, window.getOrDefault(r,0) + 1);
                //只有当字母对应的个数都相等了才算一个有效
                //注意用equals，==的话超过-128～127就会出错
                if(window.get(r).equals(need.get(r))){
                    valid ++;
                }
            }
            //只有当覆盖的串中能全部包含t才可以开始缩减左边界
            while(valid == need.size()){
                //满足条件就看一次长度，往小了取
                int temp = right - left;
                if(len > temp){
                    len = temp;
                    start = left;
                    end = right;
                }
                char l = s.charAt(left);
                left ++;
                if(need.containsKey(l)){
                    if(window.get(l).equals(need.get(l))){
                        valid --;
                    }
                    window.put(l,window.get(l) - 1);
                }
            }
        }
        return len != Integer.MAX_VALUE ? s.substring(start,end) : "";
    }
}
