package com.cyb.codetest.算法.单链表;

import com.alibaba.fastjson.JSON;

/**
 * 1 注意边界
 * 2 注意链表的指针不要弄乱
 *
 * @author cyb
 * @date 2022/4/1 下午3:40
 */
public class 判断单链表回文 {


    public static void main(String[] args) {

        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(3);
        ListNode node5 = new ListNode(2);
        ListNode node6 = new ListNode(1);

        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = node6;

        boolean palindrome = isPalindrome(node1);
        System.out.println("palindrome：" + palindrome);
//        forechListNode(node1);

    }


    /**
     * 快慢指针
     * <p>
     * 1-》2-》3-》2-》1   奇数   快指针到头
     * 慢：从1开始 经过 2 3        123
     * 快：从1开始 经过 3 1
     * <p>
     * 1-》2-》3-》3-》2-》1  偶数
     * 慢：从1开始 经过 2 3        123
     * 快：从1开始 经过 3 2
     * <p>
     * 无论奇数偶数慢指针都是经过了单链表长度的一半，这样我们就可以通过反转单链表的操作来判断是否是回文
     * <p>
     * 时间复杂度是O(n)  空间复杂度是O(1)
     *
     * @param head
     * @return
     */
    public static boolean isPalindrome(ListNode head) {

        if (head == null || head.next == null) {
            return true;
        }

        ListNode slow = head;
        ListNode fast = head;


        //反转的链表一半
        ListNode revhalf = null;
//        ListNode temp = null;
        /**
         * 1 fast != null && fast.next != null这样判断可以多循环一次使得revhalf正好是单链表一半的逆序
         * 奇数：此时循环完成，fast正好在最后一个元素， fast!=null
         * 偶数：fast==null
         *
         * 2 如果是fast != fast.next && fast != fast.next.next ，那么revhalf会少一个元素
         */
        while (fast != null && fast.next != null) {
            fast = fast.next.next;

            //临时变量保存
            ListNode next = slow.next;

            /**
             * 奇数：
             *  1->null
             *  2->1->null
             *
             *  偶数：
             *  1->null
             *  2->1->null
             *  3->2->1->null
             */
            slow.next = revhalf;
            /**
             * 奇数：
             * revhalf = 1
             * revhalf = 2
             *
             *
             * 偶数：
             * revhalf = 1
             * revhalf = 2
             */
            revhalf = slow;

            /**
             * 奇数
             * slow = 1
             * slow = 2
             * slow = 3
             *
             *
             * 偶数
             * slow = 1
             * slow = 2
             * slow = 3
             * slow = 3
             */
            slow = next;

            System.out.println("next.val:" + next.val);
            System.out.println("revhalf.val:" + revhalf.val);
            System.out.println("slow.val:" + slow.val);

        }


        //1-》2-》3-》2-》1   奇数
        //1-》2-》3-》3-》2-》1   偶数
        //说明是奇数,slow需要向后移动一个，这样才可以形成与revhalf一样的链表形式
        if (fast != null) {
            slow = slow.next;
        }

//        System.out.println("slow.val:" + slow.val);
//        forechListNode(revhalf);
//        forechListNode(slow);

        while (slow != null && revhalf != null) {
            if (slow.val != revhalf.val) {
                return false;
            }
            slow = slow.next;
            revhalf = revhalf.next;
        }

        return true;
    }


    public static void forechListNode(ListNode head) {

        while (head != null) {
            System.out.println(head.val);
            head = head.next;

        }


    }

}
