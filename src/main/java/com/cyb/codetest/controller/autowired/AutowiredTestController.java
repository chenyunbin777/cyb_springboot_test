package com.cyb.codetest.controller.autowired;

import com.cyb.codetest.service.strategy.StrategyContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cyb
 * @date 2023/8/14 上午10:51
 */
@RestController
@RequestMapping("/autowired")
@Slf4j
public class AutowiredTestController {


    @Autowired
    private StrategyContext strategyContext;

    @RequestMapping("test1")
    public String autowiredTest(){
        log.info("a哈哈");
        strategyContext.handle();
        return "a哈哈";
    }

}
