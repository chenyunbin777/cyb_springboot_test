package com.cyb.codetest.dubbo.impl;

import com.cyb.codetest.dubbo.TestSerivce;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 在dubbo 2.7.8中注解
 * @Service被@DubboService 取代。
 * @Reference被@DubboReference取代。
 * 估计是dubbo的开发团队考虑到，原来的注解和spring的原生注解重名了，为了在语言层面和spring的原生注解，有所以区别减少出错概率。
 * @author cyb
 * @date 2021/8/6 上午10:49
 */
//设置接口的版本号 和  消费的超市时长
@DubboService(version = "1.0.0",timeout = 3000)
public class TestSerivceImpl implements TestSerivce {
    @Override
    public void testDubbo() {
        System.out.println("testDubbo");
    }
}
