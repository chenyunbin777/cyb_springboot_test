package com.cyb.codetest.java异常;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author cyb
 * @date 2022/9/11 下午11:35
 */
public class RunoobExceptionTest {

    public static void main(String[] args) {
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader("test.txt"))) {
            while ((line = br.readLine()) != null) {
                System.out.println("Line =>"+line);
            }
        } catch (IOException e) {
            System.out.println("IOException in try block =>" + e.getMessage());
        }
    }
}
