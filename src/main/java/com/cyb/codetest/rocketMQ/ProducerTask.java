//package com.cyb.codetest.rocketMQ;
//
//import com.alibaba.fastjson.JSON;
//import org.apache.rocketmq.client.exception.MQBrokerException;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.client.producer.*;
//import org.apache.rocketmq.common.message.Message;
//import org.apache.rocketmq.common.message.MessageQueue;
//import org.apache.rocketmq.remoting.common.RemotingHelper;
//import org.apache.rocketmq.remoting.exception.RemotingException;
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java异常.md.io.UnsupportedEncodingException;
//import java异常.md.util.List;
//import java异常.md.util.UUID;
//
///**
// * @author cyb
// * @date 2021/1/9 3:24 下午
// */
//@Component
//public class ProducerTask {
//
//    @Resource
//    private DefaultMQProducer producer;
//
//    @Autowired
//    private RocketMQTemplate rocketMQTemplate;
//
//    /**
//     * 每5秒执行一次
//     */
//    @Scheduled(cron = "0/5 * *  * * ?")
//    private void sendMsgToMq() {
//        String str = "发送测试消息";
//        System.out.println("producer:"+ JSON.toJSONString(producer));
//
//        Message msg;
//        try {
//            msg = new Message("test-demo"
//                    , "111"
//                    , UUID.randomUUID().toString()
//                    , str.getBytes("utf-8"));
//            // TODO:报错 No route info for this topic, test-demo
//            SendResult result = producer.send(msg);
//            //异步调用
////            rocketMQTemplate.asyncSend("test-demo", msg, new SendCallback() {
////                @Override
////                public void onSuccess(SendResult sendResult) {
////                    System.out.println("生产者收到回复！！");
////                }
////
////                @Override
////                public void onException(Throwable throwable) {
////
////                }
////            });
//            if (result.getSendStatus() == SendStatus.SEND_OK) {
//                System.out.println("消息发送成功");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
