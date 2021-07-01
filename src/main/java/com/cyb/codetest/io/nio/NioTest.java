package com.cyb.codetest.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

/**
 * @author cyb
 * @date 2021/2/10 10:10 下午
 */
public class NioTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        LinkedList<SocketChannel> clents = new LinkedList<>();
        ServerSocketChannel sc = ServerSocketChannel.open();
        sc.bind(new InetSocketAddress(9090));
        sc.configureBlocking(false);//设置为非阻塞

//可以用一个线程接收所有客户端的连接，然后交由其他线程或线程池处理
        while (true) {
            Thread.sleep(1000);
            SocketChannel clent = sc.accept();//不会阻塞
            if (clent != null) {
                clent.configureBlocking(false);
                clents.add(clent);
            }

            ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
            //遍历所有客户端连接
            for (SocketChannel c : clents) {
                int read = c.read(buffer);
                if (read > 0) {
                    buffer.flip();
                    byte[] a = new byte[buffer.limit()];
                    buffer.get(a);
                    String b = new String(a);
                    buffer.clear();
                }
            }
        }
    }
}
