package com.cyb.codetest.双色球;

import com.alibaba.fastjson.JSON;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author cyb
 * @date 2022/7/19 下午3:44
 */
public class DoubleColorBall {

    //1~33   1~16
    public static void main(String[] args) throws Exception {

//        doubleColorBall(5);
        readFile();
//        test();

//        System.out.println(String.format("%.2f",0.1f));
    }

    public static void doubleColorBall(int count) {

        for (int i = 0; i < count; i++) {
            List<Integer> redBallList = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                while (true) {
                    int redBall = (int) (1 + Math.random() * (33 - 1 + 1));
                    if (!redBallList.contains(redBall)) {
                        redBallList.add(redBall);
                        break;
                    }
                }

            }
            int blueBall = (int) (1 + Math.random() * (16 - 1 + 1));
            System.out.println("红球:" + redBallList + "   蓝球:" + blueBall);
        }

    }


    public static void readFile() throws Exception {

        Map<String, Integer> redBallNumAndCount = new TreeMap<>();
        Map<String, Integer> blueBallNumAndCount = new TreeMap<>();


        Map<String, Integer> redBallNumAndRate = new TreeMap<>();
        Map<String, Integer> blueBallNumAndRate = new TreeMap<>();

        String filePath = "/Users/chenyunbin/Documents/我的idea项目/cyb_springboot_test/src/main/java/com/cyb/codetest/双色球/100day.txt";

        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String readLine = "";
        while ((readLine = bufferedReader.readLine()) != null) {

            String[] lineArr = readLine.split(" ");
            for (int i = 0; i < lineArr.length; i++) {
                if (i == 0) {
                    continue;
                }
                String key = lineArr[i];
                //蓝球
                if (i == 7) {
                    if (blueBallNumAndCount.containsKey(key)) {
                        blueBallNumAndCount.put(key, blueBallNumAndCount.get(key) + 1);
                    } else {
                        blueBallNumAndCount.put(key, 1);
                    }
                }

                //红球
                if (redBallNumAndCount.containsKey(key)) {
                    redBallNumAndCount.put(key, redBallNumAndCount.get(key) + 1);
                } else {
                    redBallNumAndCount.put(key, 1);
                }


            }
//            System.out.println(JSON.toJSONString(lineArr));
        }


        // 计算value的总和， 根据每个数字出现的概率 来生成一组随机的数字组合
        int allRedValue = 0;
        for (int value : redBallNumAndCount.values()) {
            allRedValue += value;
        }

        int allBlueValue = 0;
        for (int value : blueBallNumAndCount.values()) {
            allBlueValue += value;
        }


        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        float redRate = 0f;
        float blueRate = 0f;
        int redRateInt = 0;
        int blueRateInt = 0;
        System.out.println("redBallNumAndCount.keySet() size:" + redBallNumAndCount.keySet().size());
        System.out.println("blueBallNumAndCount.keySet() size:" + blueBallNumAndCount.keySet().size());
        for (String key : redBallNumAndCount.keySet()) {
            Integer value = redBallNumAndCount.get(key);
            float rate = (float) value / allRedValue;
            float rate2 = Float.parseFloat(String.format("%.2f", rate));
            redRate += rate2;
            redRateInt += rate2 * 100;
            redBallNumAndRate.put(key, (int) (rate2 * 100));
        }

        for (String key : blueBallNumAndCount.keySet()) {
            Integer value = blueBallNumAndCount.get(key);
            float rate = (float) value / allBlueValue;
            float rate2 = Float.parseFloat(String.format("%.2f", rate));
            blueRate += rate2;
            blueRateInt += rate2 * 100;
            blueBallNumAndRate.put(key, (int) (rate2 * 100));
        }
        System.out.println("redRate:" + redRate);
        System.out.println("blueRate:" + blueRate);
        System.out.println("redRateInt:" + redRateInt);
        System.out.println("blueRateInt:" + blueRateInt);

//        List<Map.Entry<String, Integer>> redBallNumAndCountList = new ArrayList<>(redBallNumAndCount.entrySet());
//        List<Map.Entry<String, Integer>> blueBallNumAndCountList = new ArrayList<>(blueBallNumAndCount.entrySet());
//        Collections.sort(redBallNumAndCountList,new MyComparator());
//        Collections.sort(blueBallNumAndCountList,new MyComparator());
//        System.out.println(JSON.toJSONString(redBallNumAndCountList));
//        System.out.println(JSON.toJSONString(blueBallNumAndCountList));


        List<Map.Entry<String, Integer>> redBallNumAndRateList = new ArrayList<>(redBallNumAndRate.entrySet());
        List<Map.Entry<String, Integer>> blueBallNumAndRateList = new ArrayList<>(blueBallNumAndRate.entrySet());
        Collections.sort(redBallNumAndRateList, new MyComparator());
        Collections.sort(blueBallNumAndRateList, new MyComparator());

        System.out.println(JSON.toJSONString(redBallNumAndRateList));
        System.out.println(JSON.toJSONString(blueBallNumAndRateList));


        //开始生成1~100的随机数
        //6红球
        for (int c = 0; c < 5; c++) {
            List<String> redBallList = new ArrayList<>();
            String blueBall = "7";

            for (int i = 0; i < 6; i++) {

                whileTrue:
                while (true) {
                    int randomNum = (int) (1 + Math.random() * (100 - 1 + 1));

                    int valueRange = 0;
                    for (int j = 0; j < redBallNumAndRateList.size(); j++) {
                        Map.Entry<String, Integer> stringIntegerEntry = redBallNumAndRateList.get(j);
                        int value = stringIntegerEntry.getValue();
                        String key = stringIntegerEntry.getKey();
                        valueRange += value;
                        if (randomNum <= valueRange) {

                            if (!redBallList.contains(key)) {
                                redBallList.add(key);
                                break whileTrue;
                            }

                        }
                    }
                }

            }


            //蓝球
            int randomBlueNum = (int) (1 + Math.random() * (100 - 1 + 1));
            int blueValueRange = 0;
            for (int j = 0; j < blueBallNumAndRateList.size(); j++) {
                Map.Entry<String, Integer> stringIntegerEntry = blueBallNumAndRateList.get(j);
                int value = stringIntegerEntry.getValue();
                String key = stringIntegerEntry.getKey();
                blueValueRange += value;
                if (randomBlueNum <= blueValueRange) {
                    blueBall = key;
                    break;
                }
            }

            System.out.println("红球：" + redBallList + "  蓝球：" + blueBall);

        }


    }


    static class MyComparator implements Comparator<Map.Entry<String, Integer>> {

        @Override
        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
            return o2.getValue().compareTo(o1.getValue());
//            return String.valueOf(o2.getValue()).compareTo(String.valueOf(o1.getValue()));
        }
    }


    static class MyComparatorFloat implements Comparator<Map.Entry<String, Float>> {

        @Override
        public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
            return o2.getValue().compareTo(o1.getValue());
//            return String.valueOf(o2.getValue()).compareTo(String.valueOf(o1.getValue()));
        }
    }

    public static void test() {
        Map<String, Integer> redBallNumAndCount = new TreeMap<>();


        redBallNumAndCount.put("01", 10);
        redBallNumAndCount.put("02", 11);

        redBallNumAndCount.put("03", 20);
        redBallNumAndCount.put("04", 15);
        redBallNumAndCount.put("05", 13);
        redBallNumAndCount.put("06", 9);
        redBallNumAndCount.put("07", 7);
        List<Map.Entry<String, Integer>> redBallNumAndCountList = new ArrayList<>(redBallNumAndCount.entrySet());

        Collections.sort(redBallNumAndCountList, new MyComparator());

        System.out.println(JSON.toJSONString(redBallNumAndCountList));
    }


}
