package com.cyb.codetest.spring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * https://www.cnblogs.com/joy99/p/10941543.html
 * 这个类，我们使用了注解 @Component 表明它将作为一个Spring Bean 被装配，使用注解 @Aspect 表示它是一个切面。
 * 类中只有一个方法 haha 我们使用 @Before 这个注解，表示他将在方法执行之前执行。关于这个注解后文再作解释。
 * 参数("execution(* com.sharpcj.aopdemo.test1.IBuy.buy(..))") 声明了切点，表明在该切面的切点是com.sharpcj.aopdemo.test1.Ibuy这个接口中的buy方法。
 *
 * @author cyb
 * @date 2021/1/18 3:23 下午
 */
//声明了切面
@Aspect
@Component
public class BuyAspectJ {

    // 声明了切点
    @Before("execution(* com.cyb.codetest.spring.aop.IBuy.buy(..))")
    public void haha() {
        System.out.println("男孩女孩都买自己喜欢的东西");
        puci();
    }

    @After("execution(* com.cyb.codetest.spring.aop.IBuy.buy(..))")
    public void puci() {
        System.out.println("买完了");
    }

    @AfterReturning("execution(* com.cyb.codetest.spring.aop.IBuy.buy(..))")
    public void afterReturning() {
        System.out.println("返回了");


    }

    /**
     * 可见，如果不调用该对象的 proceed() 方法，表示原目标方法被阻塞调用，当然也有可能你的实际需求就是这样。
     *
     * @param pj
     * @throws Throwable
     */
    @Around("execution(* com.cyb.codetest.spring.aop.IBuy.buy(..))")
    public void Around(ProceedingJoinPoint pj) throws Throwable {
        System.out.println("Around 之前");
        pj.proceed();
        System.out.println("Around 之后");
    }


}
