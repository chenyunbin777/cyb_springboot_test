package com.cyb.codetest.正则;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @auther cyb
 * @date 2024/7/3 10:13
 */
public class Test1 {

    //和供应商沟通，宫廷印花(短款) 总：7天/2万，宫廷印花（长款）总：1000

    public static void main(String[] args) {
//        String str = "11件/15天";
//        String regex = "\\d+件/\\d+天";
//        System.out.println( str.matches(regex));
//
//
//        String regex2 = "\\d+天/\\d+万";
////        String regex2 = "总：7天/2万";
//        String str2= "和供应商沟通，宫廷印花(短款) 总：7天/2万，宫廷印花（长款）总：1000";
//        System.out.println( str2.matches(regex2));
//
//
//        List<String> list = new ArrayList<>();
//
//        String regexStr = "总：\\d+天/\\d+万";
//
//        String matchStr = "和供应商沟通，宫廷印花(短款) 总：7天/2万，宫廷印花（长款）总：1000x,和供应商沟通，宫廷印花(短款) 总：7件/2天";
//        List<String> matcherResultList = getMatcherResultList(matchStr, regexStr);
//        System.out.println("matcherResultList:"+ JSON.toJSONString(matcherResultList));
//
//        String regexStr2 = "总：\\d+";
//        List<String> matcherResultList2 = getMatcherResultList(matchStr, regexStr2);
//        System.out.println("matcherResultList2:"+ JSON.toJSONString(matcherResultList2));
//
//
//        //数字后不跟：万Ww千百天件
//        String regexStr11 = "\\d+[^万Ww千百天件]";
//
//        String regexStr111 = "总：\\d+天/\\d+";
//
//        List<String> matcherResultList11 = getMatcherResultList(matchStr, regexStr11);
//        System.out.println("matcherResultList11:"+ JSON.toJSONString(matcherResultList11));
//
//        matchStr = matchStr.replace("：",":");
//        List<String> matchStrList = Arrays.stream(matchStr.split("总:")).collect(Collectors.toList());
//        System.out.println("matchStrList:"+ JSON.toJSONString(matchStrList));
//
//
//        String matchStr22 = "和供应商沟通，宫廷印花(短款) 总：7天/2万，宫廷印花（长款）总：1000，,和供应商沟通，宫廷印花(短款) 总：888万";
//        String regexStr22 = "[^/]\\d+万";
//
//        matchStr22 = matchStr22.replace("：",":");
//        List<String> matchStr22List = Arrays.stream(matchStr22.split("总:")).collect(Collectors.toList());
//        System.out.println("matchStr22List:"+JSON.toJSONString(matchStr22List));
//        List<String> matcherResultList22 = getMatcherResultList(matchStr22, regexStr22);
//        System.out.println("matcherResultList22:"+JSON.toJSONString(matcherResultList22));
//        for (String str22 : matchStr22List) {
//            List<String> matcherResultList223 = getMatcherResultList(str22, regexStr22);
//            System.out.println(JSON.toJSONString(matcherResultList223));
//        }
//
//        System.out.println("matcherResultList22:"+ JSON.toJSONString(matcherResultList22));
//
//        //todo 6 文字内容+XX天+文字内容+情况2）或者情况1+文字内容+XX天+文字内容+情况2）或者情况1
//        //例子：和供应商沟通，7天内发货库存数量1w，剩余部分库存20天内10000；
//        String matchStr3 = "和供应商沟通，7天内发货库存数量1w，剩余部分库存20天内10000";
//        String regexStr3 = "\\d+天\\S*\\d+w";
//        List<String> matcherResultList3 = getMatcherResultList(matchStr3, regexStr3);
//        System.out.println("matcherResultList3:"+ JSON.toJSONString(matcherResultList3));
//
//        String matchStr4 = "和供应商沟通，89天内发货库存数量111W,剩余部分库存2天内10000,和供应商沟通，7天内发货库存数量11万";
////        String matchStr4 = "71天";
//        String regexStr4 = "\\d+天\\S*\\d+[w万W千百天件]";
//        List<String> matcherResultList4 = getMatcherResultList(matchStr4, regexStr4);
//        System.out.println("matcherResultList4:"+ JSON.toJSONString(matcherResultList4));


        //匹配不包含天的数字
//        String regexStrAA = "\\d+[^\\d+天]";
//        String regexStrAA = "[\\d]+(?!天)";
//        String regexStrAA = "\\d+(天|万|w|W|千|百)";

        //正确的
//        String regexStrAA = "\\d+[天万wW千百]";
//        String regexStrAA = "\\d++(?!天|万|w|W|千|百)";

//        rule5();
        rule55();
        rule555();
//        rule6();
    }

    /**
     * 判断一个字符串是否为小数或整数
     * @param str
     * @return
     */
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


    private static void rule6() {
        String regexStrAA = "\\d+[天万wW千百]|\\d+(?!天|万|w|W|千|百)";
        String matchStr4 = "和供应商沟通，89天内发货库存数量111W,剩余部分库存2天内10000,和供应商沟通，7天内发货库存数量11万";
        List<String> matcherResultListAA = getMatcherResultList(matchStr4, regexStrAA);

        System.out.println("matcherResultListAA:" + JSON.toJSONString(matcherResultListAA));

        //XX件/XX天
        StringBuilder preInventorySumResult = new StringBuilder();
        //偶数
        boolean isSuccess = true;
        if (matcherResultListAA.size() % 2 == 0) {
            for (int i = 0; i < matcherResultListAA.size(); ) {
                String matcherResult = matcherResultListAA.get(i);
                String matcherResult_i_1 = matcherResultListAA.get(i + 1);
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


        System.out.println("preInventorySumResultStr:" + preInventorySumResultStr);
    }



    private static void rule5(){
        //和供应商沟通，宫廷印花(短款)
        String preInventorySum =
                "宫廷印花总：1天/2w ，宫廷印花总：2天/2W ，宫廷印花总：3天/2万 ，宫廷印花总：4天/2千，宫廷印花总：5天/2百 宫廷印花总：1000应商沟通 宫廷印花总：2天/200000 宫廷印花总：709798789" +  //情况4的测试集合
                "宫廷印花总：2件/6天 ，宫廷印花总：" + //情况3的测试集合
                "宫廷印花总：11万 宫廷印花总：12w 宫廷印花总：13W 宫廷印花总：14千 宫廷印花总：15百" //这个不对
                ;  //情况3的测试集合
        StringBuilder preInventorySumResult = new StringBuilder();


        // | 或者来分割，前边的判断完就不会再次出现匹配成功的情况，这样从最大的匹配一直｜到最小的匹配
        String regex = "\\d+天/\\d+[万wW千百]|\\d+天/\\d+|\\d+件/\\d+天|\\d+[万wW千百]|\\d+";
        List<String> matcherResultListR = getMatcherResultList(preInventorySum, regex);
        System.out.println("matcherResultListR:"+JSON.toJSONString(matcherResultListR));

//                String regex = "总:(\\d+[天万wW千百]|\\d+(?!天|万|w|W|千|百))";

        //和供应商沟通，宫廷印花(短款) 总：7天/2万，宫廷印花（长款）总：1000
        if (preInventorySum.contains("总")) {

            //5
            //情况1:纯数字

            //数字后不跟：万Ww千百天件，
            // 匹配之后：如果数字后没有字符就直接用，有字符需要截取掉
            String regexStr = "\\d+(?!天|万|w|W|千|百|件)";
//            String regexStr = "\\d+(?!天)|\\d+(?!万)|\\d+(?!w)|\\d+(?!W)|\\d+(?!千)|\\d+(?!百)|\\d+(?!件)";
//            String regexStr = "\\d+[^万Ww千百天件]";

            String regexStrNumber = "\\d+";
            //情况2: XX万/w/千/百，  数字前一位不能包含 /  要与情况4隔离
            String regexStr11 = "[^/]\\d+万";
            String regexStr22 = "[^/]\\d+w";
            String regexStr33 = "[^/]\\d+W";
            String regexStr44 = "[^/]\\d+千";
            String regexStr55 = "[^/]\\d+百";

            String regexStr111111 = "[^/]\\d+万|[^/]\\d+w|[^/]\\d+W|[^/]\\d+千|[^/]\\d+百|\\d+";
//            String regexStr111111 = "[?!/\\d+万]|[?!/\\d+w]|[?!/\\d+W]|[?!/\\d+千]|[?!/\\d+百]|\\d+";



            List<String> matcherResultList111111 = getMatcherResultList(preInventorySum, regexStr111111);
            System.out.println("matcherResultList111111:"+JSON.toJSONString(matcherResultList111111));

            //情况3: XX件/XX天
            String regexStr111 = "\\d+件/\\d+天";

            //情况4: XX天/XX
            String regexStr1111 = "\\d+天/\\d+(?!天|万|w|W|千|百|件)";
            String regexStr2222 = "\\d+天/\\d+万";
            String regexStr3333 = "\\d+天/\\d+w";
            String regexStr4444 = "\\d+天/\\d+W";
            String regexStr5555 = "\\d+天/\\d+千";
            String regexStr6666 = "\\d+天/\\d+百";

            // list:["和供应商沟通，宫廷印花(短款) ","7天/2万，宫廷印花（长款）","1000，,和供应商沟通，宫廷印花(短款) ","7件/2天"]
            preInventorySum = preInventorySum.replace("：", ":");
            List<String> list = Arrays.stream(preInventorySum.split("总:")).collect(Collectors.toList());
            System.out.println("list:"+JSON.toJSONString(list));
            for (String preInventorySumTmp : list) {
                System.out.println("preInventorySumTmp:"+JSON.toJSONString(preInventorySumTmp));
                //情况1:纯数字
                List<String> matcherResultList = getMatcherResultList(preInventorySumTmp, regexStr);
                System.out.println("matcherResultList:"+JSON.toJSONString(matcherResultList));
                if (CollectionUtils.isNotEmpty(matcherResultList)) {
                    for (String matcherResult : matcherResultList) {
                        if (isNumeric(matcherResult)) {
                            preInventorySumResult.append(matcherResult).append("件/15天").append(",");
                        } else {
                            preInventorySumResult.append(matcherResult.substring(0, preInventorySumResult.length() - 1)).append("件/15天").append(",");
                        }
                    }
                }


                //情况2:XX万/w/千/百，  数字前一位不能包含 /  要与情况4隔离
                List<String> matcherResultList1 = getMatcherResultList(preInventorySumTmp, regexStr11);
                System.out.println("matcherResultList1:"+JSON.toJSONString(matcherResultList1));
                if (CollectionUtils.isNotEmpty(matcherResultList1)) {
                    for (String matcherResult : matcherResultList1) {

                        String replace = matcherResult.replace("万", "")
                                .replace("w", "")
                                .replace("W", "");

                        preInventorySumResult.append(Integer.parseInt(replace) * 10000).append("件/15天").append(",");
                    }
                }
                List<String> matcherResultList2 = getMatcherResultList(preInventorySumTmp, regexStr22);
                System.out.println("matcherResultList2:"+JSON.toJSONString(matcherResultList2));
                if (CollectionUtils.isNotEmpty(matcherResultList2)) {
                    for (String matcherResult : matcherResultList2) {

                        String replace = matcherResult.replace("万", "")
                                .replace("w", "")
                                .replace("W", "");

                        preInventorySumResult.append(Integer.parseInt(replace) * 10000).append("件/15天").append(",");
                    }
                }


                List<String> matcherResultList3 = getMatcherResultList(preInventorySumTmp, regexStr33);
                System.out.println("matcherResultList3:"+JSON.toJSONString(matcherResultList3));
                if (CollectionUtils.isNotEmpty(matcherResultList3)) {
                    for (String matcherResult : matcherResultList3) {

                        String replace = matcherResult.replace("万", "")
                                .replace("w", "")
                                .replace("W", "");

                        preInventorySumResult.append(Integer.parseInt(replace) * 10000).append("件/15天").append(",");
                    }
                }


                List<String> matcherResultList4 = getMatcherResultList(preInventorySumTmp, regexStr44);
                System.out.println("matcherResultList4:"+JSON.toJSONString(matcherResultList4));
                if (CollectionUtils.isNotEmpty(matcherResultList4)) {
                    for (String matcherResult : matcherResultList4) {

                        String replace = matcherResult.replace("千", "");

                        preInventorySumResult.append(Integer.parseInt(replace) * 1000).append("件/15天").append(",");
                    }
                }


                List<String> matcherResultList5 = getMatcherResultList(preInventorySumTmp, regexStr55);
                System.out.println("matcherResultList5:"+JSON.toJSONString(matcherResultList5));
                if (CollectionUtils.isNotEmpty(matcherResultList5)) {
                    for (String matcherResult : matcherResultList5) {

                        String replace = matcherResult.replace("百", "");

                        preInventorySumResult.append(Integer.parseInt(replace) * 100).append("件/15天").append(",");
                    }
                }


                //情况3: XX件/XX天
                List<String> matcherResultList111 = getMatcherResultList(preInventorySumTmp, regexStr111);
                System.out.println("matcherResultList111:"+JSON.toJSONString(matcherResultList111));
                if (CollectionUtils.isNotEmpty(matcherResultList111)) {
                    for (String matcherResult : matcherResultList111) {
                        preInventorySumResult.append(matcherResult).append(",");
                    }
                }

                //情况4: XX天/XX
                List<String> matcherResultList1111 = getMatcherResultList(preInventorySumTmp, regexStr1111);
                System.out.println("matcherResultList1111:"+JSON.toJSONString(matcherResultList1111));
                if (CollectionUtils.isNotEmpty(matcherResultList1111)) {
                    for (String matcherResult : matcherResultList1111) {

                        List<String> dayCountList = Arrays.stream(matcherResult.split("/")).collect(Collectors.toList());
                        if (dayCountList.size() == 2) {
                            preInventorySumResult.append(Integer.parseInt(dayCountList.get(1))).append("件").append("/").append(dayCountList.get(0)).append(",");
                        }
                    }
                }

                List<String> matcherResultList2222 = getMatcherResultList(preInventorySumTmp, regexStr2222);
                List<String> matcherResultList3333 = getMatcherResultList(preInventorySumTmp, regexStr3333);
                List<String> matcherResultList4444 = getMatcherResultList(preInventorySumTmp, regexStr4444);

                System.out.println("matcherResultList2222:"+JSON.toJSONString(matcherResultList2222));
                System.out.println("matcherResultList3333:"+JSON.toJSONString(matcherResultList3333));
                System.out.println("matcherResultList4444:"+JSON.toJSONString(matcherResultList4444));

                matcherResultList2222.addAll(matcherResultList3333);
                matcherResultList2222.addAll(matcherResultList4444);

                if (CollectionUtils.isNotEmpty(matcherResultList2222)) {
                    for (String matcherResult : matcherResultList2222) {
                        String replace = matcherResult.replace("万", "").replace("w", "").replace("W", "");

                        List<String> dayCountList = Arrays.stream(replace.split("/")).collect(Collectors.toList());
                        if (dayCountList.size() == 2) {
                            preInventorySumResult.append(Integer.parseInt(dayCountList.get(1)) * 10000).append("件").append("/").append(dayCountList.get(0)).append(",");
                        }
                    }
                }

                List<String> matcherResultList5555 = getMatcherResultList(preInventorySumTmp, regexStr5555);
                if (CollectionUtils.isNotEmpty(matcherResultList5555)) {
                    for (String matcherResult : matcherResultList5555) {
                        String replace = matcherResult.replace("千", "");

                        List<String> dayCountList = Arrays.stream(replace.split("/")).collect(Collectors.toList());
                        if (dayCountList.size() == 2) {
                            preInventorySumResult.append(Integer.parseInt(dayCountList.get(1)) * 1000).append("件").append("/").append(dayCountList.get(0)).append(",");
                        }
                    }
                }

                List<String> matcherResultList6666 = getMatcherResultList(preInventorySumTmp, regexStr6666);
                if (CollectionUtils.isNotEmpty(matcherResultList6666)) {
                    for (String matcherResult : matcherResultList6666) {
                        String replace = matcherResult.replace("百", "");

                        List<String> dayCountList = Arrays.stream(replace.split("/")).collect(Collectors.toList());
                        if (dayCountList.size() == 2) {
                            preInventorySumResult.append(Integer.parseInt(dayCountList.get(1)) * 100).append("件").append("/").append(dayCountList.get(0)).append(",");
                        }
                    }
                }
            }

            String preInventorySumResultStr = "";
            if (StringUtils.isNotEmpty(preInventorySumResult)) {
                preInventorySumResultStr = preInventorySumResult.toString().substring(0, preInventorySumResult.length() - 1);
            }


            System.out.println("preInventorySumResultStr:" + preInventorySumResultStr);

        }


    }



    private static void rule55(){
        //和供应商沟通，宫廷印花(短款)
        String preInventorySum =
                "宫廷印花总：1天/2w ，宫廷印花总：2天/2W ，宫廷印花总：3天/2万 ，宫廷印花总：4天/2千，宫廷印花总：5天/2百 宫廷印花总：1000应商沟通 宫廷印花总：2天/200000 宫廷印花总：709798789" +  //情况4的测试集合
                        "宫廷印花总：2件/6天 ，宫廷印花总：" + //情况3的测试集合
                        "宫廷印花总：11万 宫廷印花总：12w 宫廷印花总：13W 宫廷印花总：14千 宫廷印花总：15百" //这个不对
                ;  //情况3的测试集合
        StringBuilder preInventorySumResult = new StringBuilder();

        String regexR  = "\\d+天/\\d+[万wW千百]|\\d+天/\\d+|\\d+件/\\d+天|\\d+[万wW千百]|\\d+";
        List<String> matcherResultListR = getMatcherResultList(preInventorySum, regexR);
        System.out.println("matcherResultListR:"+JSON.toJSONString(matcherResultListR));

        //和供应商沟通，宫廷印花(短款) 总：7天/2万，宫廷印花（长款）总：1000
        if (preInventorySum.contains("总")) {

            //5
            //情况1:纯数字

            //情况2: XX万/w/千/百，  数字前一位不能包含 /  要与情况4隔离
            String regexStr11 = "[^/]\\d+万";
            String regexStr22 = "[^/]\\d+w";
            String regexStr33 = "[^/]\\d+W";
            String regexStr44 = "[^/]\\d+千";
            String regexStr55 = "[^/]\\d+百";

            //情况3: XX件/XX天
            String regexStr111 = "\\d+件/\\d+天";

            //情况4: XX天/XX
//            String regexStr1111 = "\\d+天/\\d+[^万wW千百]";
            String regexStr1111 = "^\\d+天/\\d+";
            String regexStr2222 = "\\d+天/\\d+万";
            String regexStr3333 = "\\d+天/\\d+w";
            String regexStr4444 = "\\d+天/\\d+W";
            String regexStr5555 = "\\d+天/\\d+千";
            String regexStr6666 = "\\d+天/\\d+百";

            // list:["和供应商沟通，宫廷印花(短款) ","7天/2万，宫廷印花（长款）","1000，,和供应商沟通，宫廷印花(短款) ","7件/2天"]
            preInventorySum = preInventorySum.replace("：", ":");
            List<String> list = Arrays.stream(preInventorySum.split("总:")).collect(Collectors.toList());
            System.out.println("list:"+JSON.toJSONString(list));
            for (String preInventorySumTmp : list) {



                // | 或者来分割，前边的判断完就不会再次出现匹配成功的情况，这样从最大的匹配一直｜到最小的匹配
                String regex = "\\d+天/\\d+[万wW千百]|\\d+天/\\d+|\\d+件/\\d+天|\\d+[万wW千百]|\\d+";
                List<String> matcherResultList = getMatcherResultList(preInventorySumTmp, regex);
                System.out.println("matcherResultList:"+JSON.toJSONString(matcherResultList));


                if (CollectionUtils.isNotEmpty(matcherResultList)) {
                    for (String matcherResult : matcherResultList) {

                      if(isNumeric(matcherResult)){
                          preInventorySumResult.append(matcherResult).append("件/15天").append(",");
                      }
                    }
                }



                //情况2:XX万/w/千/百，  数字前一位不能包含 /  要与情况4隔离
                List<String> matcherResultList1 = getMatcherResultList(preInventorySumTmp, regexStr11);
                System.out.println("matcherResultList1:"+JSON.toJSONString(matcherResultList1));
                if (CollectionUtils.isNotEmpty(matcherResultList1)) {
                    for (String matcherResult : matcherResultList1) {

                        String replace = matcherResult.replace("万", "")
                                .replace("w", "")
                                .replace("W", "");

                        preInventorySumResult.append(Integer.parseInt(replace) * 10000).append("件/15天").append(",");
                    }
                }
                List<String> matcherResultList2 = getMatcherResultList(preInventorySumTmp, regexStr22);
                System.out.println("matcherResultList2:"+JSON.toJSONString(matcherResultList2));
                if (CollectionUtils.isNotEmpty(matcherResultList2)) {
                    for (String matcherResult : matcherResultList2) {

                        String replace = matcherResult.replace("万", "")
                                .replace("w", "")
                                .replace("W", "");

                        preInventorySumResult.append(Integer.parseInt(replace) * 10000).append("件/15天").append(",");
                    }
                }


                List<String> matcherResultList3 = getMatcherResultList(preInventorySumTmp, regexStr33);
                System.out.println("matcherResultList3:"+JSON.toJSONString(matcherResultList3));
                if (CollectionUtils.isNotEmpty(matcherResultList3)) {
                    for (String matcherResult : matcherResultList3) {

                        String replace = matcherResult.replace("万", "")
                                .replace("w", "")
                                .replace("W", "");

                        preInventorySumResult.append(Integer.parseInt(replace) * 10000).append("件/15天").append(",");
                    }
                }


                List<String> matcherResultList4 = getMatcherResultList(preInventorySumTmp, regexStr44);
                System.out.println("matcherResultList4:"+JSON.toJSONString(matcherResultList4));
                if (CollectionUtils.isNotEmpty(matcherResultList4)) {
                    for (String matcherResult : matcherResultList4) {

                        String replace = matcherResult.replace("千", "");

                        preInventorySumResult.append(Integer.parseInt(replace) * 1000).append("件/15天").append(",");
                    }
                }


                List<String> matcherResultList5 = getMatcherResultList(preInventorySumTmp, regexStr55);
                System.out.println("matcherResultList5:"+JSON.toJSONString(matcherResultList5));
                if (CollectionUtils.isNotEmpty(matcherResultList5)) {
                    for (String matcherResult : matcherResultList5) {

                        String replace = matcherResult.replace("百", "");

                        preInventorySumResult.append(Integer.parseInt(replace) * 100).append("件/15天").append(",");
                    }
                }


                //情况3: XX件/XX天
                List<String> matcherResultList111 = getMatcherResultList(preInventorySumTmp, regexStr111);
                System.out.println("matcherResultList111:"+JSON.toJSONString(matcherResultList111));
                if (CollectionUtils.isNotEmpty(matcherResultList111)) {
                    for (String matcherResult : matcherResultList111) {
                        preInventorySumResult.append(matcherResult).append(",");
                    }
                }

                //情况4: XX天/XX
                List<String> matcherResultList1111 = getMatcherResultList(preInventorySumTmp, regexStr1111);
                System.out.println("matcherResultList1111:"+JSON.toJSONString(matcherResultList1111));
                if (CollectionUtils.isNotEmpty(matcherResultList1111)) {
                    for (String matcherResult : matcherResultList1111) {

                        List<String> dayCountList = Arrays.stream(matcherResult.split("/")).collect(Collectors.toList());
                        if (dayCountList.size() == 2) {
                            preInventorySumResult.append(Integer.parseInt(dayCountList.get(1))).append("件").append("/").append(dayCountList.get(0)).append(",");
                        }
                    }
                }

                List<String> matcherResultList2222 = getMatcherResultList(preInventorySumTmp, regexStr2222);
                List<String> matcherResultList3333 = getMatcherResultList(preInventorySumTmp, regexStr3333);
                List<String> matcherResultList4444 = getMatcherResultList(preInventorySumTmp, regexStr4444);

                System.out.println("matcherResultList2222:"+JSON.toJSONString(matcherResultList2222));
                System.out.println("matcherResultList3333:"+JSON.toJSONString(matcherResultList3333));
                System.out.println("matcherResultList4444:"+JSON.toJSONString(matcherResultList4444));

                matcherResultList2222.addAll(matcherResultList3333);
                matcherResultList2222.addAll(matcherResultList4444);

                if (CollectionUtils.isNotEmpty(matcherResultList2222)) {
                    for (String matcherResult : matcherResultList2222) {
                        String replace = matcherResult.replace("万", "").replace("w", "").replace("W", "");

                        List<String> dayCountList = Arrays.stream(replace.split("/")).collect(Collectors.toList());
                        if (dayCountList.size() == 2) {
                            preInventorySumResult.append(Integer.parseInt(dayCountList.get(1)) * 10000).append("件").append("/").append(dayCountList.get(0)).append(",");
                        }
                    }
                }

                List<String> matcherResultList5555 = getMatcherResultList(preInventorySumTmp, regexStr5555);
                if (CollectionUtils.isNotEmpty(matcherResultList5555)) {
                    for (String matcherResult : matcherResultList5555) {
                        String replace = matcherResult.replace("千", "");

                        List<String> dayCountList = Arrays.stream(replace.split("/")).collect(Collectors.toList());
                        if (dayCountList.size() == 2) {
                            preInventorySumResult.append(Integer.parseInt(dayCountList.get(1)) * 1000).append("件").append("/").append(dayCountList.get(0)).append(",");
                        }
                    }
                }

                List<String> matcherResultList6666 = getMatcherResultList(preInventorySumTmp, regexStr6666);
                if (CollectionUtils.isNotEmpty(matcherResultList6666)) {
                    for (String matcherResult : matcherResultList6666) {
                        String replace = matcherResult.replace("百", "");

                        List<String> dayCountList = Arrays.stream(replace.split("/")).collect(Collectors.toList());
                        if (dayCountList.size() == 2) {
                            preInventorySumResult.append(Integer.parseInt(dayCountList.get(1)) * 100).append("件").append("/").append(dayCountList.get(0)).append(",");
                        }
                    }
                }
            }

            String preInventorySumResultStr = "";
            if (StringUtils.isNotEmpty(preInventorySumResult)) {
                preInventorySumResultStr = preInventorySumResult.toString().substring(0, preInventorySumResult.length() - 1);
            }


            System.out.println("preInventorySumResultStr:" + preInventorySumResultStr);

        }


    }

    private static void rule555(){
        //和供应商沟通，宫廷印花(短款)
        String preInventorySum =
                "宫廷印花总：1天/2w ，宫廷印花总：2天/2W ，宫廷印花总：3天/2万 ，宫廷印花总：4天/2千，宫廷印花总：5天/2百 宫廷印花总：1000应商沟通 宫廷印花总：2天/200000 宫廷印花总：709798789" +  //情况4的测试集合
                        "宫廷印花总：2件/6天 ，宫廷印花总：" + //情况3的测试集合
                        "宫廷印花总：11万 宫廷印花总：12w 宫廷印花总：13W 宫廷印花总：14千 宫廷印花总：15百" //这个不对
                ;  //情况3的测试集合
        StringBuilder preInventorySumResult = new StringBuilder();

        String regexR  = "\\d+天/\\d+[万wW千百]|\\d+天/\\d+|\\d+件/\\d+天|\\d+[万wW千百]|\\d+";
        List<String> matcherResultListR = getMatcherResultList(preInventorySum, regexR);
        System.out.println("matcherResultListR:"+JSON.toJSONString(matcherResultListR));

        //和供应商沟通，宫廷印花(短款) 总：7天/2万，宫廷印花（长款）总：1000
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
            System.out.println("list:" + JSON.toJSONString(list));
            for (String preInventorySumTmp : list) {


                // | 或者来分割，前边的判断完就不会再次出现匹配成功的情况，这样从最大的匹配一直｜到最小的匹配
                String regex = "\\d+天/\\d+[万wW千百]|\\d+天/\\d+|\\d+件/\\d+天|\\d+[万wW千百]|\\d+";
                List<String> matcherResultList = getMatcherResultList(preInventorySumTmp, regex);
//                System.out.println("matcherResultList:" + JSON.toJSONString(matcherResultList));


                if (CollectionUtils.isNotEmpty(matcherResultList)) {
                    for (String matcherResult : matcherResultList) {

                        if (isNumeric(matcherResult)) {
                            preInventorySumResult.append(matcherResult).append("件/15天").append(",");
                        }

                        //情况2:XX万/w/千/百，  数字前一位不能包含 /  要与情况4隔离
                        List<String> matcherResultList2 = getMatcherResultList(matcherResult, regexStr2);
//                        System.out.println("matcherResultList2:" + JSON.toJSONString(matcherResultList2));
                        if (CollectionUtils.isNotEmpty(matcherResultList2)) {
                            for (String matcherResult2 : matcherResultList2) {

                                if (matcherResult2.contains("万") || matcherResult2.contains("w") || matcherResult2.contains("W")) {
                                    String replace = matcherResult2.replace("万", "")
                                            .replace("w", "")
                                            .replace("W", "");

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
//                        System.out.println("matcherResultList3:" + JSON.toJSONString(matcherResultList3));
                        if (CollectionUtils.isNotEmpty(matcherResultList3)) {
                            for (String matcherResult3 : matcherResultList3) {
                                preInventorySumResult.append(matcherResult3).append(",");
                            }
                        }

                        //情况4: XX天/XX
                        List<String> matcherResultList4 = getMatcherResultList(preInventorySumTmp, regexStr4);
                        System.out.println("matcherResultList4:" + JSON.toJSONString(matcherResultList4));
                        if (CollectionUtils.isNotEmpty(matcherResultList4)) {
                            for (String matcherResult4 : matcherResultList4) {

                                List<String> dayCountList = Arrays.stream(matcherResult4.split("/")).collect(Collectors.toList());
                                if (dayCountList.size() == 2) {
                                    String day = dayCountList.get(0);
                                    String num = dayCountList.get(1);

                                    if (num.contains("万") || num.contains("w") || num.contains("W")) {
                                        String replace = num.replace("万", "")
                                                .replace("w", "")
                                                .replace("W", "");

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
                }

            }

            String preInventorySumResultStr = "";
            if (StringUtils.isNotEmpty(preInventorySumResult)) {
                preInventorySumResultStr = preInventorySumResult.toString().substring(0, preInventorySumResult.length() - 1);
            }
            System.out.println("preInventorySumResultStr:" + preInventorySumResultStr);

        }
    }
}
