package com.cyb.codetest.练手;

import java.util.ArrayList;

/**
 * @author cyb
 * @date 2022/11/1 下午5:57
 */
public class TestWaitSignal {
    //储存元素的队列
    public ArrayList<Integer> list = new ArrayList<>();

    //队列最大长度5
    private int maxSize = 5;

    public void put(){

        synchronized (list){
            if(list.size() < maxSize){
                System.out.println("开始生产总数量："+list.size());
                list.add(1);
                list.notify();
            }else {
                try {
                    //生产队列满了等待
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void get(){

        synchronized (list){
            if(list.size() > 0){
                System.out.println("开始消费总数量："+list.size());
                list.remove(0);
                list.notify();
            }else {
                //消费队列为空 等待生产
                try {
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }




}
