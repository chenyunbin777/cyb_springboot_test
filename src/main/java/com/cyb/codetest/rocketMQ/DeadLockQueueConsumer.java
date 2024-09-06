package com.cyb.codetest.rocketMQ;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 死信队列消费者
 * @author cyb
 * @date 2024/9/2 下午3:05
 */
public class DeadLockQueueConsumer {

    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("DLQConsumerGroup");
        consumer.setNamesrvAddr("localhost:9876");

        consumer.subscribe("TopicTest", "*");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    try {
                        // 模拟消息处理
                        if (new String(msg.getBody()).contains("Error")) {
                            throw new RuntimeException("消费异常");
                        }
                        System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), new String(msg.getBody()));
                    } catch (Exception e) {
                        if (msg.getReconsumeTimes() >= 3) {
                            // 消息重试超过3次，转移到死信队列
                            System.out.printf("消息消费失败且超过重试次数，转移到死信队列: %s %n", new String(msg.getBody()));
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        } else {
                            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                        }
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        System.out.printf("Consumer Started.%n");
    }

//    原文链接：https://blog.csdn.net/FireFox1997/article/details/140871106
}
