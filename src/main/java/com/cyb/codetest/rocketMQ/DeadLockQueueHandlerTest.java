package com.cyb.codetest.rocketMQ;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * https://www.cnblogs.com/duanxz/p/3896994.html
 * 死信队列处理，可以单独补偿处理
 * @author cyb
 * @date 2024/9/2 下午3:02
 */
public class DeadLockQueueHandlerTest {
    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("DLQHandlerGroup");

        DefaultMQPullConsumer defaultMQPullConsumer = new DefaultMQPullConsumer();

        consumer.setNamesrvAddr("localhost:9876");

        //死信队列topic是自动生成的 需要重新定义消费者，
        //可以写一个脚本，传入参数就是死信topic
        consumer.subscribe("%DLQ%DLQConsumerGroup", "*");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    // 处理死信消息
                    System.out.printf("处理死信消息: %s %n", new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        System.out.printf("DLQ Handler Started.%n");
    }

//    原文链接：https://blog.csdn.net/FireFox1997/article/details/140871106
}
