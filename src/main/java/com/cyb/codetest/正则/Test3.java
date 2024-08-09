package com.cyb.codetest.正则;

import org.apache.commons.math3.geometry.euclidean.oned.SubOrientedPoint;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther cyb
 * @date 2024/7/9 19:40
 */
public class Test3 {


    public static void main(String[] args) {
        String groupNames = "asfqwegrehet";
        List<String> groupNameList = Arrays.stream(groupNames.split(",")).collect(Collectors.toList());
        System.out.println(groupNameList);
    }
}
