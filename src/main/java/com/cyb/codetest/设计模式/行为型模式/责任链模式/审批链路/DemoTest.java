package com.cyb.codetest.设计模式.行为型模式.责任链模式.审批链路;

/**
 * @author cyb
 * @date 2022/9/10 下午4:01
 */
public class DemoTest {

    public static void main(String[] args) {
        AbstractApproval managerApproval = new ManagerApproval(1);
        AbstractApproval bossApproval = new BossApproval(2);
        AbstractApproval superBossApproval = new SuperBossApproval(3);

        managerApproval.next = bossApproval;
        bossApproval.next = superBossApproval;

        managerApproval.process(3);
    }
}
