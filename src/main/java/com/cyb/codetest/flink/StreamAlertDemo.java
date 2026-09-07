//package com.cyb.codetest.flink;
//
//import org.apache.flink.api.common.serialization.SimpleStringSchema;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.datastream.DataStreamSource;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//
//import java.util.Properties;
//
///**
// * https://www.jb51.net/article/273062.htm
// * @author cyb
// * @date 2024/9/20 上午8:08
// */
//public class StreamAlertDemo {
//    public static void main(String[] args) throws Exception {
//        //我们可以使用 Flink 提供的 StreamExecutionEnvironment 来创建一个流式处理的作业。下面是一个简单的示例代码：
//        //我们首先使用 StreamExecutionEnvironment.getExecutionEnvironment() 获取一个执行环境，设置并行度
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(3);
//        Properties properties = new Properties();
//        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        //作为kafka的消费者
//        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<>("test", new SimpleStringSchema(), properties);
//        DataStreamSource<String> inputDataStream = env.addSource(kafkaConsumer);
//        DataStream<String> resultStream = inputDataStream.flatMap(new AlertFlatMapper());
//        resultStream.print().setParallelism(4);
//        resultStream.addSink(new FlinkKafkaProducer<String>("demo",new SimpleStringSchema(),properties));
//        env.execute();
//    }
//
//}
