package com.cyb.codetest.spring.aop;

import org.springframework.stereotype.Component;

/**
 * @author cyb
 * @date 2021/1/18 3:25 下午
 */
@Component
public class Girl implements IBuy {
    @Override
    public String buy() {
        System.out.println("女孩买了一件漂亮的衣服");
        return "衣服";
    }
}
