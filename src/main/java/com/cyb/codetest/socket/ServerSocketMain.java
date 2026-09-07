package com.cyb.codetest.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author cyb
 * @date 2025/4/9 下午4:19
 */
public class ServerSocketMain {


    public static void main(String[] args) {
        try {
            System.out.println("ServerSocket开始");
            ServerSocket serverSocket = new ServerSocket(8888);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                // 处理客户端连接，可开启新线程进行数据交互

                //获取输入输出流：
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();

                //数据读写
                byte[] buffer = new byte[1024];
                int length = inputStream.read(buffer);
                String receivedData = new String(buffer, 0, length);
                outputStream.write("Response".getBytes());

                //关闭资源
                inputStream.close();
                outputStream.close();
                clientSocket.close();
                serverSocket.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
