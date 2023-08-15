package com.cyb.codetest.service.strategy;

import com.alibaba.fastjson.JSON;
import com.cyb.codetest.service.StrategyTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cyb
 * @date 2023/8/14 上午10:56
 */
@Component
public class StrategyContext {


    @Autowired
    private final Map<String, StrategyTestService> map = new HashMap<>();

    public StrategyContext(Map<String, StrategyTestService> map) {
//        this.map.clear();
        map.forEach(this.map::put);
    }


    public void handle(){
        System.out.println(JSON.toJSONString(map));
    }


}
