package com.cyb.codetest.设计模式.行为型模式.责任链模式.审批链路;

/**
 * @author cyb
 * @date 2022/9/10 下午4:00
 */
public class SuperBossApproval extends AbstractApproval {


    public SuperBossApproval(int approvalLevel) {
        super(approvalLevel);
    }

    @Override
    public void process(int approvalLevel) {
        System.out.println("超级老板审批");
    }


}
