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
 * @date 2024/7/9 14:22
 */
public class TestPre {

    public static void main(String[] args) {

        String importPreInventorySum2 = "1百";
        String importPreInventorySum22 = getImportPreInventorySum(importPreInventorySum2);
        System.out.println("importPreInventorySum22:" + importPreInventorySum22);

//        String importPreInventorySum3 = "20000/15天,3w/15天，4W/15天，5万/15天，1千/15天，1百/15天，6件/15天，7个/15天，8组/15天，9盒/15天";
//        String importPreInventorySum44 = getImportPreInventorySum(importPreInventorySum3);
//        System.out.println("importPreInventorySum44:" + importPreInventorySum44);


//        String importPreInventorySum4 = "15天/20000,15天/3w，15天/4W，15天/5万，15天/1千，15天/1百，15天/6件，15天/7个，15天/8组，15天/9盒,153天/1000";
//        String importPreInventorySum44 = getImportPreInventorySum(importPreInventorySum4);
//        System.out.println("importPreInventorySum44:"+importPreInventorySum44);


//        String importPreInventorySum5 = "和供应商沟通，宫廷印花(短款) 总：7天/2万，和供应商沟通，宫廷印花(短款) 总：7天/3盒，和供应商沟通，宫廷印花(短款) 总：10盒/100天，和供应商沟通，宫廷印花(短款) 总：7天/389，宫廷印花（长款）总：1000、宫廷印花（长款）总：2000件、宫廷印花（长款）总：3000组、宫廷印花（长款）总：4000个、宫廷印花（长款）总：5000盒，safe我222盒，阿个人过热34554,245w";
//        String importPreInventorySum55 = getImportPreInventorySum(importPreInventorySum5);
//        System.out.println("importPreInventorySum55:" + importPreInventorySum55);

//        String preInventorySumTmp6 = "和供应商沟通，7天内发货库存数量1件，剩余部分库存20天内10000组,剩余部分库存20天内20000个,剩余部分库存20天内30000盒）";
//        String importPreInventorySum66 = getImportPreInventorySum(preInventorySumTmp6);
//        System.out.println("importPreInventorySum66:"+importPreInventorySum66);

//        StringBuilder preInventorySumResult = new StringBuilder();
//        getImportPreInventorySumRule3("10盒/100天", preInventorySumResult);
//        System.out.println("preInventorySumResult:"+preInventorySumResult);

        List<String> groupNameList = Arrays.asList("   aaaa    ");
        List<String> stringList = groupNameList.stream().map(String::trim).collect(Collectors.toList());
        System.out.println(stringList);

    }


    /**
     * 预售库存处理
     *
     * @param preInventorySum
     * @return
     */
    private static String getImportPreInventorySum(String preInventorySum) {

        if (StringUtils.isBlank(preInventorySum)) {
            return "";
        }

        String ruleOneOrTwoOfSpotPre = getRuleOneOrTwoOfSpotPre(preInventorySum, 2);
        if (StringUtils.isNotEmpty(ruleOneOrTwoOfSpotPre)) {
            return ruleOneOrTwoOfSpotPre;
        }

        //规则3 XX件/XX天，XX件/XX天，XX件/XX天，XX件/XX天
//        String regexCondition3 = "\\d+件/\\d+天";
//
        List<String> preInventorySumList = Arrays.stream(preInventorySum.split("，|,")).collect(Collectors.toList());

//        for (String preInventorySumTmp : preInventorySumList) {
//
//            if (preInventorySumTmp.matches(regexCondition3)) {
//                preInventorySumResult.append(preInventorySumTmp).append(",");
//            }
//        }
        StringBuilder preInventorySumResult = new StringBuilder();
        getImportPreInventorySumRule3(preInventorySum, preInventorySumResult);

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

        //规则7 XX/XX天
        for (String preInventorySumTmp : preInventorySumList) {
            getImportPreInventorySumRule7(preInventorySumTmp, preInventorySumResult);
        }

        if (StringUtils.isNotEmpty(preInventorySumResult)) {
            return preInventorySumResult.substring(0, preInventorySumResult.length() - 1);
        }

        return "-1";

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

    /**
     * @param matchStr:需要匹配的字符串 如 abc123wdef4562w
     * @param regexStr:\d+w
     * @return
     */
    private static String getMatcherResultOne(String matchStr, String regexStr, boolean isGetIndex1) {
        List<String> list = new ArrayList<>();

        Pattern pattern = Pattern.compile(regexStr);
        Matcher matcher = pattern.matcher(matchStr);

        while (matcher.find()) {
            list.add(matcher.group());
        }

        //直接获取第一个匹配的字符
        if (CollectionUtils.isNotEmpty(list) && isGetIndex1) {
            return list.get(0);
        }

        if (list.size() == 1) {
            return list.get(0);
        }
        return null;

    }


    /**
     * 预售库存规则7
     * “15天预售库存”导入时新增识别场景：XXX/XX天，其中前面为库存量需要支持纯数字或者XX万/w/千/百（w支持大小写），后面为预售周期只支持纯数字；均去除空格和换行再解析；
     * 例：1000/15天，1w/15天
     * 涉及范围包括：直播计划播前和播后（盖亚+迈科斯），供应商端，播后商品清单
     *
     * @param preInventorySumTmp
     * @param preInventorySumResult
     */
    private static void getImportPreInventorySumRule7(String preInventorySumTmp, StringBuilder preInventorySumResult) {

        //情况7:XX/XX天
        String regexStr7 = "\\d+[万wW千百]/\\d+天|\\d+/\\d+天";

        List<String> matcherResultList7 = getMatcherResultList(preInventorySumTmp, regexStr7);
        System.out.println("getImportPreInventorySumRule7 matcherResultList7:" + JSON.toJSONString(matcherResultList7));
        //["1000/15天","1w/15天","1W/15天","1万/15天","1千/15天","1百/15天"]
        if (CollectionUtils.isNotEmpty(matcherResultList7)) {
            for (String matcherResult4 : matcherResultList7) {

                List<String> dayCountList = Arrays.stream(matcherResult4.split("/")).collect(Collectors.toList());
                if (dayCountList.size() == 2) {
                    String num = dayCountList.get(0);
                    String day = dayCountList.get(1);

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
                        preInventorySumResult.append(num.replaceAll("\n", "").replaceAll(" ", "")).append("件/").append(day).append(",");
                    }

                }
            }
        }

    }

    private static List<String> getMatcherResultList(String matchStr, String regexStr) {
        List<String> list = new ArrayList<>();

        Pattern pattern = Pattern.compile(regexStr);
        Matcher matcher = pattern.matcher(matchStr);

        while (matcher.find()) {
            list.add(matcher.group());
        }

        return list;
    }


    private static void getImportPreInventorySumRule4(String preInventorySumTmp, StringBuilder preInventorySumResult) {

        //情况4: XX天/XX
        String regexStr4 = "\\d+天/\\d+[万wW千百件组个盒]|\\d+天/\\d+";

        List<String> matcherResultList4 = getMatcherResultList(preInventorySumTmp, regexStr4);
//        System.out.println("getImportPreInventorySumRule4 matcherResultList4:" + JSON.toJSONString(matcherResultList4));
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
                    } else if (num.contains("件")
                            || num.contains("组")
                            || num.contains("个")
                            || num.contains("盒")) {
                        String replace = num.replaceAll("\n", "").replaceAll(" ", "").replace("件", "").replace("组", "").replace("个", "").replace("盒", "");

                        preInventorySumResult.append(replace).append("件/").append(day).append(",");
                    } else {
                        preInventorySumResult.append(num.replaceAll("\n", "").replaceAll(" ", "")).append("件/").append(day).append(",");
                    }

                }
            }
        }

    }


    /**
     * 15天预售库存 规则6
     * 文字内容+XX天+文字内容+情况2）或者情况1+文字内容+XX天+文字内容+情况2）或者情况1
     * 例子：和供应商沟通，7天内发货库存数量1w，剩余部分库存20天内10000；
     *
     * @return
     */
    private static String getImportPreInventorySumRule6(String preInventorySum) {
        String regexStr = "\\d+[天万wW千百件组个盒]|\\d++(?!天|万|w|W|千|百|件|组|个|盒)";
        List<String> matcherResultList = getMatcherResultList(preInventorySum, regexStr);

        System.out.println("getImportPreInventorySumRule6 matcherResultList:{}" + JSON.toJSONString(matcherResultList));

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
                    String replace = matcherResult_i_1.replaceAll("\n", "").replaceAll(" ", "").replace("万", "")
                            .replace("w", "")
                            .replace("W", "");

                    preInventorySumResult.append(Integer.parseInt(replace) * 10000).append("件/").append(matcherResult).append(",");
                }
                if (matcherResult_i_1.contains("千")) {
                    String replace = matcherResult_i_1.replaceAll("\n", "").replaceAll(" ", "").replace("千", "");

                    preInventorySumResult.append(Integer.parseInt(replace) * 1000).append("件/").append(matcherResult).append(",");
                }
                if (matcherResult_i_1.contains("百")) {
                    String replace = matcherResult_i_1.replaceAll("\n", "").replaceAll(" ", "").replace("百", "");

                    preInventorySumResult.append(Integer.parseInt(replace) * 100).append("件/").append(matcherResult).append(",");
                }

                if (matcherResult_i_1.contains("件")
                        || matcherResult_i_1.contains("组")
                        || matcherResult_i_1.contains("个")
                        || matcherResult_i_1.contains("盒")) {
                    String replace = matcherResult_i_1.replaceAll("\n", "").replaceAll(" ", "").replace("件", "").replace("组", "").replace("个", "").replace("盒", "");

                    preInventorySumResult.append(replace).append("件/").append(matcherResult).append(",");
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


    /**
     * XX件/XX天（件/组/个/盒）均支持识别为件
     */
    private static void getImportPreInventorySumRule3(String preInventorySum, StringBuilder preInventorySumResult) {

        String regex1 = "\\d+[件组个盒]/\\d+天";
//        String regex2 = "\\d+组/\\d+天";
//        String regex3 = "\\d+个/\\d+天";
//        String regex4 = "\\d+盒/\\d+天";
        List<String> preInventorySumList = Arrays.stream(preInventorySum.split("，|,")).collect(Collectors.toList());
        for (String preInventorySumTmp : preInventorySumList) {

            if (preInventorySumTmp.matches(regex1)) {

                List<String> dayCountList = Arrays.stream(preInventorySumTmp.split("/")).collect(Collectors.toList());
                if (dayCountList.size() == 2) {
                    String num = dayCountList.get(0);
                    String day = dayCountList.get(1);
                    String replace = num.replaceAll("\n", "").replaceAll(" ", "").replace("件", "").replace("组", "").replace("个", "").replace("盒", "");

                    preInventorySumResult.append(replace).append("件/").append(day).append(",");
                }

            }

        }

        System.out.println("getImportPreInventorySumRule3:" + preInventorySumResult);

    }

    /**
     * 现货、预售库存规则1 or 2处理
     *
     * @param sportOrPreInventorySum
     * @param sportOrPre             1 现货 2 预售
     * @return
     */
    private static String getRuleOneOrTwoOfSpotPre(String sportOrPreInventorySum, int sportOrPre) {

        if (isNumeric(sportOrPreInventorySum)) {

            if (sportOrPre == 1) {
                return sportOrPreInventorySum;
            }
            if (sportOrPre == 2) {
                return sportOrPreInventorySum + "件/15天";
            }
        }

        //预售库存新增
        if (sportOrPre == 2) {
            String regex = "\\d+[件组个盒]";

            if (sportOrPreInventorySum.matches(regex)) {
                String replace = sportOrPreInventorySum.replaceAll("\n", "")
                        .replaceAll(" ", "")
                        .replace("件", "")
                        .replace("组", "")
                        .replace("个", "")
                        .replace("盒", "");

                return replace + "件/15天";
            }
        }


        //rule 1 和 rule 2
        String regex1 = "\\d+万";
        String regex2 = "\\d+w";
        String regex3 = "\\d+W";
        String regex4 = "\\d+千";
        String regex5 = "\\d+百";

        if (sportOrPreInventorySum.matches(regex1)
                || sportOrPreInventorySum.matches(regex2)
                || sportOrPreInventorySum.matches(regex3)) {//XX万/w/千/百（w支持大小写）

            String replace = sportOrPreInventorySum.replaceAll("\n", "")
                    .replaceAll(" ", "")
                    .replace("万", "")
                    .replace("w", "")
                    .replace("W", "");
            if (isNumeric(replace)) {

                if (sportOrPre == 1) {
                    return String.valueOf(Integer.parseInt(replace) * 10000);
                }
                if (sportOrPre == 2) {
                    return Integer.parseInt(replace) * 10000 + "件/15天";
                }
            }
        } else if (sportOrPreInventorySum.matches(regex4)) {
            String replace = sportOrPreInventorySum.replaceAll("\n", "")
                    .replaceAll(" ", "")
                    .replace("千", "");
            if (isNumeric(replace)) {

                if (sportOrPre == 1) {
                    return String.valueOf(Integer.parseInt(replace) * 1000);
                }
                if (sportOrPre == 2) {
                    return Integer.parseInt(replace) * 1000 + "件/15天";
                }


            }
        } else if (sportOrPreInventorySum.matches(regex5)) {
            String replace = sportOrPreInventorySum.replaceAll("\n", "")
                    .replaceAll(" ", "")
                    .replace("百", "");
            if (isNumeric(replace)) {
                if (sportOrPre == 1) {
                    return String.valueOf(Integer.parseInt(replace) * 100);
                }
                if (sportOrPre == 2) {
                    return Integer.parseInt(replace) * 100 + "件/15天";
                }
            }
        }

        return "";
    }


    /**
     * 15天预售库存 规则5
     *
     * @param preInventorySum
     */
    private static String getImportPreInventorySumRule5(String preInventorySum) {
        StringBuilder preInventorySumResult = new StringBuilder();

        if (preInventorySum.contains("总")) {

            //5
            //情况1:纯数字

            //情况2: XX万/w/千/百，  数字前一位不能包含 /  要与情况4隔离
            String regexStr2 = "\\d+[万wW千百件组个盒]";

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
//                String regex = "\\d+天/\\d+[万wW千百]|\\d+天/\\d+|\\d+件/\\d+天|\\d+[万wW千百]|\\d+";
                String regex = "\\d+天/\\d+[万wW千百件组个盒]|\\d+天/\\d+|\\d+[件组个盒]/\\d+天|\\d+[万wW千百件组个盒]|\\d+";
                List<String> matcherResultList = getMatcherResultList(preInventorySumTmp, regex);
//                System.out.println("getImportPreInventorySumRule5 matcherResultList:" + JSON.toJSONString(matcherResultList));

                if (CollectionUtils.isNotEmpty(matcherResultList)) {
                    for (String matcherResult : matcherResultList) {

                        if (isNumeric(matcherResult)) {
                            preInventorySumResult.append(matcherResult).append("件/15天").append(",");
                        }

                        //情况2:XX万/w/千/百

                        if (matcherResult.matches(regexStr2)) {
                            if (matcherResult.contains("万") || matcherResult.contains("w") || matcherResult.contains("W")) {
                                String replace = matcherResult.replaceAll("\n", "").replaceAll(" ", "").replace("万", "").replace("w", "").replace("W", "");

                                preInventorySumResult.append(Integer.parseInt(replace) * 10000).append("件/15天").append(",");
                            }

                            if (matcherResult.contains("千")) {
                                String replace = matcherResult.replaceAll("\n", "").replaceAll(" ", "").replace("千", "");

                                preInventorySumResult.append(Integer.parseInt(replace) * 1000).append("件/15天").append(",");
                            }

                            if (matcherResult.contains("百")) {
                                String replace = matcherResult.replaceAll("\n", "").replaceAll(" ", "").replace("百", "");

                                preInventorySumResult.append(Integer.parseInt(replace) * 100).append("件/15天").append(",");
                            }

                            if (matcherResult.contains("件")
                                    || matcherResult.contains("组")
                                    || matcherResult.contains("个")
                                    || matcherResult.contains("盒")) {
                                String replace = matcherResult.replaceAll("\n", "").replaceAll(" ", "").replace("件", "").replace("组", "").replace("个", "").replace("盒", "");

                                preInventorySumResult.append(replace).append("件/15天").append(",");
                            }
                        }


                        //\\d+件组个盒/\d+天
                        getImportPreInventorySumRule3(matcherResult, preInventorySumResult);

                    }


                    //情况3: XX件/XX天
//                        List<String> matcherResultList3 = getMatcherResultList(preInventorySumTmp, regexStr3);
//                        log.info("getImportPreInventorySumRule5 matcherResultList3:" + JSON.toJSONString(matcherResultList3));
//                        if (CollectionUtils.isNotEmpty(matcherResultList3)) {
//                            for (String matcherResult3 : matcherResultList3) {
//                                preInventorySumResult.append(matcherResult3).append(",");
//                            }
//                        }


                    //情况4: XX天/XX
                    getImportPreInventorySumRule4(preInventorySumTmp, preInventorySumResult);
                }
            }


            String preInventorySumResultStr = "";
            if (StringUtils.isNotEmpty(preInventorySumResult)) {
                preInventorySumResultStr = preInventorySumResult.toString().substring(0, preInventorySumResult.length() - 1);
            }
//            System.out.println("getImportPreInventorySumRule5 preInventorySumResultStr:" + preInventorySumResultStr);
            return preInventorySumResultStr;
        }

        return "";
    }
}
