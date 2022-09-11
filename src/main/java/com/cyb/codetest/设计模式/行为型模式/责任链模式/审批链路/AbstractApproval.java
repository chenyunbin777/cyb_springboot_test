package com.cyb.codetest.设计模式.行为型模式.责任链模式.审批链路;

/**
 * 审批抽象类
 *
 * @author cyb
 * @date 2022/9/10 下午3:57
 */
public abstract class AbstractApproval {

    AbstractApproval next;

    /**
     * 审批登记 越大审批登记越高
     */
    int approvalLevel;

    public AbstractApproval(int approvalLevel) {
        this.approvalLevel = approvalLevel;
    }


    /**
     * 抽象处理方法
     */
    public abstract void process(int approvalLevel);
}
