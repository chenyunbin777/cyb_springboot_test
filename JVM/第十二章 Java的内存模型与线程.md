# Java的内存模型
- 主内存
- 线程的工作内存

- volatile型变量的特殊规则
    - 对所有线程的可见性：对volatile修饰的变量的所有写操作都能立刻反映到其他线程中
        - volatile变量会在修改之后同步会主内存，然后其他线程会将主内存中的值刷新到线程的本地内存中
        - synchronized final也可以保证可见性
    - 禁止指令重排序优化：
    - 不具有原子性：导致在并发下一样是不安全的，需要加锁来保证原子性


- 对于long和double型变量的特殊规则
    - 目前商用虚拟机几乎都选择把long和double型变量读写操作作为原子操作。
    - 不需要把long和double型变量专门声明为volatile


- 先行发生原则（happen-before）Java内存模型中定义的两项操作之间的偏序关系。
    -（1）程序次序规则：在一个线程内，按照程序代码顺序，书写在前面的操作先行发生于后面的操作
    -（2）管程锁定规则：一个unlock先行发生于同一个锁下的lock操作。
    -（3）volatile：对于一个volatile变量的写操作先行发生于读操作。
    -（4）传递性：A先行发生于B，B先行发生于C，A就先行发生于C。
    -（5）线程启动规则，终止规则，终端规则
    -（6）对象终结规则：一个对象的初始化完成（构造函数执行结束）先行发生于他的finalize方法的开始。
    
- 线程的实现       
    
```
/**
 * 双检锁单例：来说明volatile的禁止指令重排序的能力
 * 指令重排序：是指CPU采用了允许将多条指令不按程序规定的顺序分开发送给各相应电路单元处理。最终会保证的出正确的结果
 * @author cyb
 * @date 2020/12/30 5:09 下午
 */
public class DoubleCheckLock {

    private volatile static DoubleCheckLock instance;

    public static DoubleCheckLock getInstance() {
        if(instance == null){
            synchronized (DoubleCheckLock.class){
                if(instance == null){
                    // 内部过程
                    //1 给实例分配内存
                    //2 初始化Singeton()构造器
                    //3 将instance对象指向分配的内存（instance非null了）
                    // volatile的作用就是让着按照1 2 3 的顺序来执行（通过内存屏障来实现），如果没有volatile修饰，就有可能让3不在最后执行，
                    // 那么 另外的线程也执行同样的代码在判断if(instance == null)时instance不为null了，return instance会报错的。
                    instance = new DoubleCheckLock();
                }
            }
        }


        return instance;
    }
}

```