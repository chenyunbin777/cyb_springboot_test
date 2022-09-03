package com.cyb.codetest.算法.第五章_高频面试题;

import com.cyb.codetest.算法.ListNode;

/**
 * @author cyb
 * @date 2022/9/1 下午11:13
 */
public class 删除排序链表中的重复元素_83 {

    public ListNode deleteDuplicates(ListNode head) {

        if(head == null){
            return head;
        }
        ListNode slow = head;
        ListNode fast = head.next;

        while(fast != null){

            if(fast.val != slow.val){
                // System.out.println(fast.val);
                // 将slow与fast位置连接，并且跳转到fast位置
                slow.next = fast;
                slow = slow.next;

            }
            fast = fast.next;
        }

        // while(head!=null){

        //     System.out.println(head.val);
        //      head = head.next;
        // }
        //断开与后面重复元素的连接
        slow.next = null;
        return head;


    }
}
