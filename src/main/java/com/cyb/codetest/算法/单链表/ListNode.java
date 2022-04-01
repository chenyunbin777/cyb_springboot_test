package com.cyb.codetest.算法.单链表;

import lombok.Data;

/**
 * @author cyb
 * @date 2022/4/1 下午3:41
 */
@Data
public class ListNode {

    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
    }
}
