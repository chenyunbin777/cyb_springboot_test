package com.cyb.codetest.正则;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @auther cyb
 * @date 2024/7/12 13:49
 */
public class Test11 {

    public static void main(String[] args) {
        StringBuilder preInventorySumResult = new StringBuilder();
        getImportPreInventorySumRule4("12天/10000,15天/2w",preInventorySumResult);

        System.out.println("preInventorySumResult:"+preInventorySumResult);
    }


    private static void getImportPreInventorySumRule4(String preInventorySumTmp, StringBuilder preInventorySumResult){

        //情况4: XX天/XX
        String regexStr4 = "\\d+天/\\d+[万wW千百]|^\\d+天/\\d+";

        List<String> matcherResultList4 = getMatcherResultList(preInventorySumTmp, regexStr4);

        System.out.println("matcherResultList4:"+JSON.toJSONString(matcherResultList4));
        if (CollectionUtils.isNotEmpty(matcherResultList4)) {
            for (String matcherResult4 : matcherResultList4) {

                List<String> dayCountList = Arrays.stream(matcherResult4.split("/")).collect(Collectors.toList());
                if (dayCountList.size() == 2) {
                    String day = dayCountList.get(0);
                    String num = dayCountList.get(1);

                    if (num.contains("万") || num.contains("w") || num.contains("W")) {
                        String replace = num.replaceAll("\n", "").replaceAll(" ", "").replace("万", "").replace("w", "").replace("W", "");

                        preInventorySumResult.append(Integer.parseInt(replace) * 10000).append("件/").append(day).append(",");
                    } else if (num.contains("千")) {
                        String replace = num.replaceAll("\n", "").replaceAll(" ", "").replace("千", "");

                        preInventorySumResult.append(Integer.parseInt(replace) * 1000).append("件/").append(day).append(",");
                    } else if (num.contains("百")) {
                        String replace = num.replaceAll("\n", "").replaceAll(" ", "").replace("百", "");

                        preInventorySumResult.append(Integer.parseInt(replace) * 100).append("件/").append(day).append(",");
                    } else {
                        preInventorySumResult.append(num.replaceAll("\n", "").replaceAll(" ", "")).append("件/").append(num).append(",");
                    }

                }
            }
        }

    }

    private   static List<String> getMatcherResultList( String matchStr,String regexStr){
        List<String> list = new ArrayList<>();

        Pattern pattern = Pattern.compile(regexStr);
        Matcher matcher = pattern.matcher(matchStr);

        while (matcher.find()) {
            list.add(matcher.group());
        }

        return list;
    }
}
