package com.cyb.codetest;

import com.cyb.codetest.spring.aop.Boy;
import com.cyb.codetest.spring.aop.Girl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class CodetestApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private Boy boy;

    @Autowired
    private Girl girl;

    @Test
    public void testAop() {
        boy.buy();
        girl.buy();
    }

}
