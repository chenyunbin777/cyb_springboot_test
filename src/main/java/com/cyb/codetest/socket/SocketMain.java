package com.cyb.codetest.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 客户端
 * @author cyb
 * @date 2025/4/9 下午4:22
 */
public class SocketMain {


    public static void main(String[] args) {
        try {

            Socket socket = new Socket("127.0.0.1", 8888);
            // 连接成功后可进行数据读写操作

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();


            outputStream.write("Message".getBytes());
            byte[] buffer = new byte[1024];
            int length = inputStream.read(buffer);
            String receivedData = new String(buffer, 0, length);

            System.out.println("服务端返回");

            inputStream.close();
            outputStream.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


