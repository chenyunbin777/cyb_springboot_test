package com.cyb.codetest.多线程.forkJoin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * @auther cyb
 * @date 2024/1/30 16:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListTask extends RecursiveTask<Boolean> {

    private List<Integer> list;


    @Override
    protected Boolean compute() {

        if (list.size() < 3) {
            System.out.println("执行任务");
            for (Integer integer : list) {
                System.out.println(integer);
            }


        } else {
            System.out.println("执行任务划分");
            int middle = list.size() / 2;
            List<Integer> subtaskList1 = list.subList(0, middle);
            List<Integer> subtaskList2 = list.subList(middle + 1, list.size());
            ListTask subtask1 = new ListTask(subtaskList1);
            ListTask subtask2 = new ListTask(subtaskList2);

            subtask1.fork();
            subtask2.fork();

            subtask1.join();
            subtask2.join();

        }

        return true;
    }
}
