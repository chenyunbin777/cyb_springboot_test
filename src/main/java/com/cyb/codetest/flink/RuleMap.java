//package com.cyb.codetest.flink;
//
//import com.alibaba.fastjson.JSON;
//
//import java.util.List;
//
///**
// * @author cyb
// * @date 2024/9/20 上午8:12
// */
//public class RuleMap {
//    private RuleMap(){}
//    public final static Map<String,List<AlertRule>> initialRuleMap;
//    private static List<AlertRule> ruleList = new ArrayList<>();
//    private static List<String> ruleStringList = new ArrayList<>(Arrays.asList(
//            "{\"target\":\"MathVal\",\"type\":\"0\",\"criticalVal\":90,\"descInfo\":\"You Math score is too low\"}",
//            "{\"target\":\"MathVal\",\"type\":\"2\",\"criticalVal\":140,\"descInfo\":\"You Math score is too high\"}",
//            "{\"target\":\"PhysicsVal\",\"type\":\"0\",\"criticalVal\":60,\"descInfo\":\"You Physics score is too low\"}",
//            "{\"target\":\"PhysicsVal\",\"type\":\"2\",\"criticalVal\":95,\"descInfo\":\"You Physics score is too high\"}"));
//    static {
//        for (String i : ruleStringList) {
//            ruleList.add(JSON.parseObject(i, AlertRule.class));
//        }
//        initialRuleMap = ruleList.stream().collect(Collectors.groupingBy(AlertRule::getTarget));
//    }
//}