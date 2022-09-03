package com.cyb.codetest.算法.第五章_高频面试题;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cyb
 * @date 2022/9/1 下午11:26
 */
public class 最长回文子串 {


    public static void main(String[] args) {

        最长回文子串 Main = new 最长回文子串();

        String longestPalindrome = Main.longestPalindrome("babad");
        System.out.println("longestPalindrome:"+longestPalindrome);


    }
    public String longestPalindrome(String s) {

        String max = "";
        for (int i = 0; i < s.length(); i++) {
            String jiShu = longestPalindrome(s, i, i);
            String ouShu = longestPalindrome(s, i, i + 1);
            String maxTemp = jiShu.length() >= ouShu.length() ? jiShu : ouShu;
            max = max.length() < maxTemp.length() ? maxTemp : max;
        }


        return max;
    }


    /**
     * 求最长回文子串，以left，right为中心， left相等为奇数序列，不相等为偶数
     *
     * @param left
     * @param right
     * @return
     */
    public String longestPalindrome(String s, int left, int right) {

        while (left >= 0 && right< s.length() && s.charAt(left) == s.charAt(right)) {

            //从中心向两边扩散
            left--;
            right++;
        }
        return s.substring(left+1, right);

    }

}
