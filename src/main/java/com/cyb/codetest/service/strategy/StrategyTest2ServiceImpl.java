package com.cyb.codetest.service.strategy;

import com.cyb.codetest.service.StrategyTestService;
import org.springframework.stereotype.Service;

/**
 * @author cyb
 * @date 2023/8/14 上午10:52
 */
@Service("StrategyTest2ServiceImpl")
public class StrategyTest2ServiceImpl implements StrategyTestService {

    @Override
    public void strategyTest() {
        System.out.println("strategyTest2");
    }
}
