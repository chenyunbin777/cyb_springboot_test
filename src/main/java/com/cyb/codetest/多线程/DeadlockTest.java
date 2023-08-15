package com.cyb.codetest.多线程;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cyb
 * @date 2023/2/6 下午3:42
 */
public class DeadlockTest {

    public static void main(String[] args) {

        ClassLoader classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                return super.loadClass(name);
            }
        };

        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();

        ExecutorService exectuorService = Executors.newFixedThreadPool(2);

        exectuorService.submit(() -> {
            lock1.lock();
            try{
                Thread.sleep(1000);

                lock2.lock();
            }catch(Exception e){}
            try{}
            finally{
                lock2.unlock();
                lock1.unlock();
            }
        });
        exectuorService.submit(() -> {
            lock2.lock();
            try{
                Thread.sleep(1000);
                lock1.lock();
            }catch(Exception e){}

            try{}
            finally{
                lock1.unlock();
                lock2.unlock();
            }
        });
    }
}
