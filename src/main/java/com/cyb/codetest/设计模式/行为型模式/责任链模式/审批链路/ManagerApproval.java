package com.cyb.codetest.设计模式.行为型模式.责任链模式.审批链路;

/**
 * @author cyb
 * @date 2022/9/10 下午4:00
 */
public class ManagerApproval extends AbstractApproval {


    public ManagerApproval(int approvalLevel) {
        super(approvalLevel);
    }

    @Override
    public void process(int approvalLevel) {
        if(this.approvalLevel >= approvalLevel){
            System.out.println("经理审批");
            return;
        }
        next.process(approvalLevel);

    }
}
