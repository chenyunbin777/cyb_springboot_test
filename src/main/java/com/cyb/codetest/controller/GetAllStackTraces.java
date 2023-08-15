package com.cyb.codetest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author cyb
 * @date 2022/8/15 上午10:50
 */
@RestController
@Slf4j
@RequestMapping("/test")
public class GetAllStackTraces {

    @RequestMapping("getAllStackTraces")
    public void GetAllStackTraces(){

        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
        for (Thread thread:allStackTraces.keySet()) {
            StackTraceElement[] stackTraceElements = allStackTraces.get(thread);

            for (StackTraceElement stackTraceElement:stackTraceElements) {

                log.info("线程:{}堆栈信息stackTraceElement:{}",thread.getName(),stackTraceElement);

            }
        }

    }

}
