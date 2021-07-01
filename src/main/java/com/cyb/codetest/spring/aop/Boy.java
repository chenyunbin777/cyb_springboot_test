package com.cyb.codetest.spring.aop;

import org.springframework.stereotype.Component;

/**
 * @author cyb
 * @date 2021/1/18 3:25 下午
 */
@Component
public class Boy implements IBuy {
    @Override
    public String buy() {
        System.out.println("男孩买了一个游戏机");
        return "游戏机";
    }
}
