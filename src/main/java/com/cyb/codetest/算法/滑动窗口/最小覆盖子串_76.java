package com.cyb.codetest.算法.滑动窗口;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cyb
 * @date 2022/8/28 下午4:11
 */
public class 最小覆盖子串_76 {


    public static void main(String[] args) {

        String minSubString = minWindow("aa", "aa");
        System.out.println(minSubString);
    }


    public static String minWindow(String s, String t) {
        Map<Character, Integer> need = new HashMap<Character, Integer>();
        Map<Character, Integer> window = new HashMap<Character, Integer>();

        //计算出每个字符的个数
        for (int i = 0; i < t.length(); i++) {
            char c = t.charAt(i);
            need.put(c, need.getOrDefault(c, 0) + 1);
        }


        int left = 0, right = 0;
        //表示窗口中满足need条件的字符个数
        int valid = 0;

        //记录最小覆盖子串起始索引以及长度
        int start = 0, end = 0, len = Integer.MAX_VALUE;

        while (right < s.length()) {
            char right_element = s.charAt(right);

            //保证end是最小子串的末尾+1，因为subString是不包含end
            right++;

            //判断need中是否存在c ,如果存在就放入窗口
            if (need.containsKey(right_element)) {
                window.put(right_element, window.getOrDefault(right_element, 0) + 1);
                //如果窗口中的c的字母个数与need中一样 vaild++
                if (window.get(right_element) == need.get(right_element)) {
                    valid++;

                }
            }

            System.out.println(JSON.toJSONString(window));

            //判断左侧窗口是否要收缩
            while (valid == need.size() && left <= right) {
                //更新滑动窗口
                if (right - left < len) {
                    start = left;
                    end = right;
                    len = right - left;
                }


                char left_element = s.charAt(left);
                left++;
                //如果存在t中的元素，需要从window中移除,
                if (need.containsKey(left_element)) {
                    if (window.get(left_element) == need.get(left_element)) {
                        valid--;
                    }
                    window.put(left_element, window.getOrDefault(left_element, 0) - 1);
                }

            }

        }

        if (len != Integer.MAX_VALUE) {
            return s.substring(start, end);
        }

        return "";
    }
}
