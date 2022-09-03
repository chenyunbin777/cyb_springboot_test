package com.cyb.codetest.算法.第四章;

import java.util.Stack;

/**
 * @author cyb
 * @date 2022/9/2 下午12:30
 */
public class 计算器2 {

    /**
     *  + - （） 空格计算器
     * @param s
     * @return
     */
    public static int calculate(String s) {
//        计算结果，部分计算结果，括号中计算结果
        int res = 0;
//        当前的数字，例如：1+23中的1或者23
        int num = 0;
//        符号，加号(+1)或者减号(-1)
        int sign = 1;
//        当右括号时，用于存储计算结果
        Stack<Integer> stack = new Stack<>();

        char[] chars = s.toCharArray();
        int len = chars.length;

        for (int i = 0; i < len; i++) {
            char c = chars[i];
//            如果当前字符为' '，则直接continue
            if (c == ' ') {
                continue;
            }
//            如果当前字符是一个数字，则用num进行记录
//            当前有可能是一个>9的数字，所以需要num = num * 10 + c - '0'
            if (c >= '0' && c <= '9') {
                num = num * 10 + c - '0';
//                判断当前数字是否已经取完
//                例如：123+4，只有当取到+时，才能确定123为当前的num
                if (i < len - 1 && '0' <= chars[i + 1] && chars[i + 1] <= '9') {
                    continue;
                }
//            如果当前字符为'+'或者'-'
            } else if (c == '+' || c == '-') {
//                将num置为0，用来存放当前符号(+/-)之后的数字
                num = 0;
//                如果当前符号为+，则sign为+1
//                如果当前符号为-，则sign为-1
                sign = c == '+' ? 1 : -1;
//            如果当前符号为'('
            } else if (c == '(') {
//                例如当前表达式为：'123+(...)'
//                则将res:123，入栈
                stack.push(res);
//                则将sign:+，入栈
                stack.push(sign);
//                同时res置为0，用来保存()中的计算结果
                res = 0;
//                sign置为初始状态，为1
                sign = 1;
//            如果当前符号为')'
            } else if (c == ')') {
//                '('前边的符号出栈
                sign = stack.pop();
//                将num替换为括号中的计算结果
                num = res;
//                将res替换为括号前边的计算结果
                res = stack.pop();
            }
//            TODO 每遍历一次，得到一个res，每次计算所得的结果
            res += sign * num;
        }
        return res;
    }
}
