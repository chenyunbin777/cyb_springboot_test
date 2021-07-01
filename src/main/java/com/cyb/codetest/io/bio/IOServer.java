package com.cyb.codetest.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @author cyb
 * @date 2021/2/10 9:06 下午
 */
public class IOServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("socket = " + socket);
            new Thread(() -> {
                try {
                    InputStream in = socket.getInputStream();
                    OutputStream out = socket.getOutputStream();
                    out.write("hello! I get your message that is follow".getBytes(Charset.forName("UTF-8")));
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) != -1) {
                        System.out.print(new String(buf, 0, len, Charset.forName("UTF-8")));
                        out.write(buf, 0, len);
                    }
                    out.write("\n end \n".getBytes(Charset.forName("UTF-8")));
                    out.flush();
                    socket.shutdownInput();
                    socket.shutdownOutput();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
