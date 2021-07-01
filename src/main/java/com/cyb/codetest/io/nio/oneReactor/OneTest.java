package com.cyb.codetest.io.nio.oneReactor;

import java.io.IOException;

/**
 * @author cyb
 * @date 2021/2/13 5:34 下午
 */
public class OneTest {
    public static void main(String[] args) throws IOException {
        TCPReactor tcpReactor = new TCPReactor(3333);
        tcpReactor.run();
    }
}
