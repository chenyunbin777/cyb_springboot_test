package com.cyb.codetest.JVM;

/**
 * Finalize逃脱gc的回收测试
 * <p>
 * 就是对象如果重写了一次finalize()方法的话，并且将自己的this指针赋值给某个类变量或者对象的成员变量，
 * 那么当gc的时候就可以 "逃过一劫"，但是当第二次在gc的时候就不会存活了，因为finalize()最多只会被系统自动调用一次。
 * <p>
 * <p>
 * 测试结果：
 * finalize method executed
 * im still alive!
 * im dead!
 *
 * @author cyb
 * @date 2020/12/25 4:41 下午
 */
public class FinalizeEscapeGCTest {

    //类变量
    public static FinalizeEscapeGCTest EscapeInstance = null;

    @Override
    protected void finalize() throws Throwable {
        //只能被系统调用一次
        super.finalize();
        System.out.println("finalize method executed");
        FinalizeEscapeGCTest.EscapeInstance = this;
    }

    public static void main(String[] args) throws InterruptedException {

        EscapeInstance = new FinalizeEscapeGCTest();
        EscapeInstance = null;
        System.gc();
        //因为finalize方法优先级很低，暂停500ms等待一下
        Thread.sleep(500);

        if (EscapeInstance != null) {
            System.out.println("im still alive!");
        } else {
            System.out.println("im dead!");
        }

        //与上部分代码完全相同
        EscapeInstance = null;
        System.gc();


        Thread.sleep(500);

        if (EscapeInstance != null) {
            System.out.println("im still alive!");
        } else {
            System.out.println("im dead!");
        }


    }
}
