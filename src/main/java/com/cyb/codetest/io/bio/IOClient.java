package com.cyb.codetest.io.bio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author cyb
 * @date 2021/2/10 9:05 下午
 */
public class IOClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8080);
        InputStream in = socket.getInputStream();
        new Thread(() -> {
            BufferedInputStream bufferIn = new BufferedInputStream(in);
            byte[] buf = new byte[1024];
            try {
                int len;
                while ((len = bufferIn.read(buf)) != -1) {
                    System.out.print(new String(buf, 0, len, Charset.forName("UTF-8")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                socket.shutdownInput();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
        OutputStream out = socket.getOutputStream();
        int cout = 10;
        while (cout-- > 0) {
            out.write(("this time is " + System.currentTimeMillis() + "\n").getBytes("UTF-8"));
        }
        socket.shutdownOutput();
    }
}
