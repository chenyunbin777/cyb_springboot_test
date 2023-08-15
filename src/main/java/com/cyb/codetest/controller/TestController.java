package com.cyb.codetest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cyb
 * @date 2023/2/21 下午5:42
 */
@RestController
@Slf4j
@RequestMapping("/test")
public class TestController {


    @RequestMapping("test1")
    public String test1(){
        log.info("a哈哈");
        return "a哈哈";
    }


}
