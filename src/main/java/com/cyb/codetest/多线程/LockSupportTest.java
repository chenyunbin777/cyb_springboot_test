package com.cyb.codetest.多线程;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

/**
 * 这儿park和unpark其实实现了wait和notify的功能，不过还是有一些差别的。
 * <p>
 * 1 park不需要获取某个对象的锁
 * 2 因为中断的时候park不会抛出InterruptedException异常，所以需要在park之后自行判断中断状态，然后做额外的处理
 * <p>
 * 作者：juconcurrent
 * 链接：https://www.jianshu.com/p/f1f2cd289205
 * 来源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 *
 * @author cyb
 * @date 2021/1/14 11:39 下午
 */
public class LockSupportTest {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        // 实现线程B
        final Thread threadB = new Thread(() -> {
            if (list.size() != 5) {
                System.out.println("线程B阻塞");
                LockSupport.park();//无期限暂停当前线程
            }
            System.out.println("线程B收到通知，开始执行自己的业务...");
        });
        // 实现线程A
        Thread threadA = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                list.add("abc");
                System.out.println("线程A向list中添加一个元素，此时list中的元素个数为：" + list.size());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (list.size() == 5)
                    LockSupport.unpark(threadB);
            }
        });
        threadA.start();
        threadB.start();
    }
}
