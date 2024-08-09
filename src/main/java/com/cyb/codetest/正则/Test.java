package com.cyb.codetest.正则;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @auther cyb
 * @date 2024/7/4 21:37
 */
public class Test {

    public static void main(String[] args) {
//        String preInventorySum = "和供应商沟通，宫廷印花(短款) 总：7天/2万，宫廷印花（长款）总：1000" ;
//        String preInventorySum = " 和供应商沟通，7天内发货库存数量1w，剩余部分库存20天内10000；" ;
//        String preInventorySum = "7天/2万，7天/3万，7天/4万，7天/3千，7天/2百" ;
        String preInventorySum = "700998件/2天，700545件/2天，70768件/2天" ;


        String importPreInventorySum = getImportPreInventorySum(preInventorySum);
        System.out.println("importPreInventorySum:"+importPreInventorySum);
    }

    /**
     * 预售库存处理
     * @param preInventorySum
     * @return
     */
    private static String getImportPreInventorySum(String preInventorySum) {

        if (StringUtils.isBlank(preInventorySum)) {
            return "";
        }

        //规则1 纯数字，系统视为XX件/15天
        if (isNumeric(preInventorySum)) {
            return preInventorySum + "件/15天";
        }

        //规则2 XX万/w/千/百（w支持大小写），转换为数字后，系统视为XX件/15天；如：2万
        String regexCount2 = "\\d+万";
        String regexCount3 = "\\d+w";
        String regexCount4 = "\\d+W";
        String regexCount5 = "\\d+千";
        String regexCount6 = "\\d+百";


        if (preInventorySum.matches(regexCount2)
                || preInventorySum.matches(regexCount3)
                || preInventorySum.matches(regexCount4)) {

            String replace = preInventorySum.replace("万", "")
                    .replace("w", "")
                    .replace("W", "");

            StringBuilder sb = new StringBuilder();
            sb.append(Integer.parseInt(replace) * 10000).append("件")
                    .append("/15天");
            return sb.toString();
        } else if (preInventorySum.matches(regexCount5)) {
            StringBuilder sb = new StringBuilder();
            String replace = preInventorySum.replace("千", "");
            sb.append(Integer.parseInt(replace) * 1000).append("件")
                    .append("/15天");
            return sb.toString();
        } else if (preInventorySum.matches(regexCount6)) {
            StringBuilder sb = new StringBuilder();
            String replace = preInventorySum.replace("百", "");
            sb.append(Integer.parseInt(replace) * 1000).append("件")
                    .append("/15天");
            return sb.toString();
        }


        //规则3 XX件/XX天，XX件/XX天，XX件/XX天，XX件/XX天
        String regexCondition3 = "\\d+件/\\d+天";

        List<String> preInventorySumList = Arrays.stream(preInventorySum.split("，|,")).collect(Collectors.toList());
        StringBuilder preInventorySumResult = new StringBuilder();
        System.out.println("preInventorySumList:"+JSON.toJSONString(preInventorySumList));
        for (String preInventorySumTmp : preInventorySumList) {

            if (preInventorySumTmp.matches(regexCondition3)) {
                preInventorySumResult.append(preInventorySumTmp).append(",");
            }
        }

        if (StringUtils.isNotEmpty(preInventorySumResult)) {
            return preInventorySumResult.substring(0, preInventorySumResult.length() - 1);
        }

        //15天预售库存 规则5优先级高于规则4 所以在前执行
        String importPreInventorySumRule5 = getImportPreInventorySumRule5(preInventorySum);
        if (StringUtils.isNotBlank(importPreInventorySumRule5)) {
            return importPreInventorySumRule5;
        }

        //规则4 XX天/XX，XX天/XX
        for (String preInventorySumTmp : preInventorySumList) {
            getImportPreInventorySumRule4(preInventorySumTmp, preInventorySumResult);
        }

        if (StringUtils.isNotEmpty(preInventorySumResult)) {
            return preInventorySumResult.substring(0, preInventorySumResult.length() - 1);
        }




        //15天预售库存 规则6
        String importPreInventorySumRule6 = getImportPreInventorySumRule6(preInventorySum);
        if (StringUtils.isNotBlank(importPreInventorySumRule6)) {
            return importPreInventorySumRule6;
        }

        return "";

    }




    private static void getImportPreInventorySumRule4(String preInventorySumTmp, StringBuilder preInventorySumResult){

        //情况4: XX天/XX
        String regexStr4 = "\\d+天/\\d+[万wW千百]|^\\d+天/\\d+";

        List<String> matcherResultList4 = getMatcherResultList(preInventorySumTmp, regexStr4);
        System.out.println("getImportPreInventorySumRule4 matcherResultList4:" + JSON.toJSONString(matcherResultList4));
        if (CollectionUtils.isNotEmpty(matcherResultList4)) {
            for (String matcherResult4 : matcherResultList4) {

                List<String> dayCountList = Arrays.stream(matcherResult4.split("/")).collect(Collectors.toList());
                if (dayCountList.size() == 2) {
                    String day = dayCountList.get(0);
                    String num = dayCountList.get(1);

                    if (num.contains("万") || num.contains("w") || num.contains("W")) {
                        String replace = num.replace("万", "").replace("w", "").replace("W", "");

                        preInventorySumResult.append(Integer.parseInt(replace) * 10000).append("件/").append(day).append(",");
                    } else if (num.contains("千")) {
                        String replace = num.replace("千", "");

                        preInventorySumResult.append(Integer.parseInt(replace) * 1000).append("件/").append(day).append(",");
                    } else if (num.contains("百")) {
                        String replace = num.replace("百", "");

                        preInventorySumResult.append(Integer.parseInt(replace) * 100).append("件/").append(day).append(",");
                    } else {
                        preInventorySumResult.append(num).append("件/").append(num).append(",");
                    }

                }
            }
        }

    }


    /**
     * 15天预售库存 规则5
     * @param preInventorySum
     */
    private static String getImportPreInventorySumRule5(String preInventorySum) {
        StringBuilder preInventorySumResult = new StringBuilder();

        if (preInventorySum.contains("总")) {

            //5
            //情况1:纯数字

            //情况2: XX万/w/千/百，  数字前一位不能包含 /  要与情况4隔离
            String regexStr2 = "[^/]\\d+[万wW千百]";

            //情况3: XX件/XX天
            String regexStr3 = "\\d+件/\\d+天";

            //情况4: XX天/XX
            String regexStr4 = "\\d+天/\\d+[万wW千百]|^\\d+天/\\d+";

            // list:["和供应商沟通，宫廷印花(短款) ","7天/2万，宫廷印花（长款）","1000，,和供应商沟通，宫廷印花(短款) ","7件/2天"]
            preInventorySum = preInventorySum.replace("：", ":");
            List<String> list = Arrays.stream(preInventorySum.split("总:")).collect(Collectors.toList());
            System.out.println("getImportPreInventorySumRule5 list:" + JSON.toJSONString(list));
            for (String preInventorySumTmp : list) {

                // | 或者来分割，前边的判断完就不会再次出现匹配成功的情况，这样从最大的匹配一直｜到最小的匹配
                String regex = "\\d+天/\\d+[万wW千百]|\\d+天/\\d+|\\d+件/\\d+天|\\d+[万wW千百]|\\d+";
                List<String> matcherResultList = getMatcherResultList(preInventorySumTmp, regex);
                System.out.println("getImportPreInventorySumRule5 matcherResultList:" + JSON.toJSONString(matcherResultList));

                if (CollectionUtils.isNotEmpty(matcherResultList)) {
                    for (String matcherResult : matcherResultList) {

                        if (isNumeric(matcherResult)) {
                            preInventorySumResult.append(matcherResult).append("件/15天").append(",");
                        }

                        //情况2:XX万/w/千/百，  数字前一位不能包含 /  要与情况4隔离
                        List<String> matcherResultList2 = getMatcherResultList(matcherResult, regexStr2);
                        System.out.println("getImportPreInventorySumRule5 matcherResultList2:" + JSON.toJSONString(matcherResultList2));
                        if (CollectionUtils.isNotEmpty(matcherResultList2)) {
                            for (String matcherResult2 : matcherResultList2) {

                                if (matcherResult2.contains("万") || matcherResult2.contains("w") || matcherResult2.contains("W")) {
                                    String replace = matcherResult2.replace("万", "").replace("w", "").replace("W", "");

                                    preInventorySumResult.append(Integer.parseInt(replace) * 10000).append("件/15天").append(",");
                                }

                                if (matcherResult2.contains("千")) {
                                    String replace = matcherResult2.replace("千", "");

                                    preInventorySumResult.append(Integer.parseInt(replace) * 1000).append("件/15天").append(",");
                                }

                                if (matcherResult2.contains("百")) {
                                    String replace = matcherResult2.replace("百", "");

                                    preInventorySumResult.append(Integer.parseInt(replace) * 100).append("件/15天").append(",");
                                }
                            }
                        }


                        //情况3: XX件/XX天
                        List<String> matcherResultList3 = getMatcherResultList(preInventorySumTmp, regexStr3);
                        System.out.println("getImportPreInventorySumRule5 matcherResultList3:" + JSON.toJSONString(matcherResultList3));
                        if (CollectionUtils.isNotEmpty(matcherResultList3)) {
                            for (String matcherResult3 : matcherResultList3) {
                                preInventorySumResult.append(matcherResult3).append(",");
                            }
                        }

                        //情况4: XX天/XX
                        getImportPreInventorySumRule4(preInventorySumTmp, preInventorySumResult);

                    }
                }

            }

            String preInventorySumResultStr = "";
            if (StringUtils.isNotEmpty(preInventorySumResult)) {
                preInventorySumResultStr = preInventorySumResult.toString().substring(0, preInventorySumResult.length() - 1);
            }
            System.out.println("getImportPreInventorySumRule5 preInventorySumResultStr:" + preInventorySumResultStr);
            return preInventorySumResultStr;
        }

        return "";
    }


    /**
     * 15天预售库存 规则6
     * 文字内容+XX天+文字内容+情况2）或者情况1+文字内容+XX天+文字内容+情况2）或者情况1
     * 例子：和供应商沟通，7天内发货库存数量1w，剩余部分库存20天内10000；
     * @return
     */
    private static String getImportPreInventorySumRule6(String preInventorySum) {
        String regexStr = "\\d+[天万wW千百]|\\d++(?!天|万|w|W|千|百)";
        List<String> matcherResultList = getMatcherResultList(preInventorySum, regexStr);

        System.out.println("getImportPreInventorySumRule6 matcherResultList:"+JSON.toJSONString(matcherResultList));

        //XX件/XX天
        StringBuilder preInventorySumResult = new StringBuilder();
        //偶数
        boolean isSuccess = true;
        if (matcherResultList.size() % 2 == 0) {
            for (int i = 0; i < matcherResultList.size(); ) {
                String matcherResult = matcherResultList.get(i);
                String matcherResult_i_1 = matcherResultList.get(i + 1);
                if (!matcherResult.contains("天")) {
                    isSuccess = false;
                    break;
                }

                if (isNumeric(matcherResult_i_1)) {
                    preInventorySumResult.append(matcherResult_i_1).append("件").append("/").append(matcherResult).append(",");
                }

                //万件
                if (matcherResult_i_1.contains("万")
                        || matcherResult_i_1.contains("w")
                        || matcherResult_i_1.contains("W")) {
                    String replace = matcherResult_i_1.replace("万", "")
                            .replace("w", "")
                            .replace("W", "");

                    preInventorySumResult.append(Integer.parseInt(replace) * 10000).append("件/").append(matcherResult).append(",");
                }
                if (matcherResult_i_1.contains("千")
                ) {
                    String replace = matcherResult_i_1.replace("千", "");

                    preInventorySumResult.append(Integer.parseInt(replace) * 1000).append("件/").append(matcherResult).append(",");
                }
                if (matcherResult_i_1.contains("百")) {
                    String replace = matcherResult_i_1.replace("百", "");

                    preInventorySumResult.append(Integer.parseInt(replace) * 100).append("件/").append(matcherResult).append(",");
                }
                i += 2;
            }
        }

        String preInventorySumResultStr = "";
        if (StringUtils.isNotEmpty(preInventorySumResult)) {
            preInventorySumResultStr = preInventorySumResult.toString().substring(0, preInventorySumResult.length() - 1);
        }

        return preInventorySumResultStr;
    }


    public static boolean isNumeric(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*\\.?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    private static List<String> getMatcherResultList( String matchStr,String regexStr){
        List<String> list = new ArrayList<>();

        Pattern pattern = Pattern.compile(regexStr);
        Matcher matcher = pattern.matcher(matchStr);

        while (matcher.find()) {
            list.add(matcher.group());
        }

        return list;
    }
}
