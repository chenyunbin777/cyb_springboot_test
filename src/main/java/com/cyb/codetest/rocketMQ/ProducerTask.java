package com.cyb.codetest.rocketMQ;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
//import java异常.md.io.UnsupportedEncodingException;
//import java异常.md.util.List;
//import java异常.md.util.UUID;

/**
 * @author cyb
 * @date 2021/1/9 3:24 下午
 */
@Component
public class ProducerTask {

    @Resource
    private DefaultMQProducer producer;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 每5秒执行一次
     */
    @Scheduled(cron = "0/5 * *  * * ?")
    private void sendMsgToMq() {
        String str = "发送测试消息";
        System.out.println("producer:"+ JSON.toJSONString(producer));
        Message msg;
        try {
            msg = new Message("test-demo"
                    , "111"
                    , UUID.randomUUID().toString()
                    , str.getBytes("utf-8"));
            // TODO:报错 No route info for this topic, test-demo


            producer.setRetryAnotherBrokerWhenNotStoreOK(true);

            //发送失败时重拾次数
            producer.setRetryTimesWhenSendAsyncFailed(2);

            //1、普通消息
            SendResult result = producer.send(msg);
            //异步调用
//            rocketMQTemplate.asyncSend("test-demo", msg, new SendCallback() {
//                @Override
//                public void onSuccess(SendResult sendResult) {
//                    System.out.println("生产者收到回复！！");
//                }
//
//                @Override
//                public void onException(Throwable throwable) {
//
//                }
//            });
            if (result.getSendStatus() == SendStatus.SEND_OK) {
                System.out.println("消息发送成功");
            }

            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("消息发送成功 生产者收到回复");
                }

                @Override
                public void onException(Throwable throwable) {
                    System.out.println("消息发送异常");

                }
            });


            //2、顺序消息（分区有序） 选择将消息发送到哪个分区
            Integer hashKey = 123;
            producer.send(msg, new MessageQueueSelector() {

                //select表示选择哪个分区发送消息   list是topic的分区队列
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object o) {

                    //算法
                    Integer id = (Integer)o;
                    int index = id % list.size();

                    //返回一个分区队列
                    return list.get(index);
                }
            },hashKey);

            //3、延迟消息 一共是18个延迟级别
            msg.setDelayTimeLevel(18);

            //4、事务消息
            TransactionMQProducer transactionMQProducer = new TransactionMQProducer("aaa");
//            transactionMQProducer.setTransactionListener();

//            transactionMQProducer.sendMessageInTransaction(msg, new LocalTransactionExecuter() {
//                @Override
//                public LocalTransactionState executeLocalTransactionBranch(Message message, Object o) {
//                    return null;
//                }
//            });

            //5、单向消息 用于日志传输等消息，允许消息丢失
            producer.setNamesrvAddr("");
            producer.setInstanceName("setInstanceName");
            producer.start();

            producer.sendOneway(msg);

            //6、批量消息发送
            List<Message> msgList  = new ArrayList<>();
            msgList.add(msg);
            msgList.add(msg);
            msgList.add(msg);
            producer.send(msgList);

            //关闭消息
            producer.shutdown();

//            org.apache.rocketmq.store.CommitLog

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
