//package com.cyb.codetest.flink;
//
//import org.apache.flink.api.common.functions.FlatMapFunction;
//import org.apache.flink.util.Collector;
//
///**
// * @author cyb
// * @date 2024/9/20 上午8:13
// */
//public class AlertFlatMapper implements FlatMapFunction<String, String> {
//    @Override
//    public void flatMap(String inVal, Collector<String> out) throws Exception {
//        Achievement user = JSON.parseObject(inVal, Achievement.class);
//        Map<String, List<AlertRule>> initialRuleMap = RuleMap.initialRuleMap;
//        List<AlertInfo> resList = new ArrayList<>();
//        List<AlertRule> mathRule = initialRuleMap.get("MathVal");
//        for (AlertRule rule : mathRule) {
//            if (checkVal(user.getMathVal(), rule.getCriticalVal(), rule.getType())) {
//                resList.add(new AlertInfo(user.getName(), rule.getDescInfo()));
//            }
//        }
//        List<AlertRule> physicsRule = initialRuleMap.get("PhysicsVal");
//        for (AlertRule rule : physicsRule) {
//            if (checkVal(user.getPhysicsVal(), rule.getCriticalVal(), rule.getType())) {
//                resList.add(new AlertInfo(user.getName(), rule.getDescInfo()));
//            }
//        }
//        String result = JSON.toJSONString(resList);
//        out.collect(result);
//    }
//    private static boolean checkVal(Integer actVal, Integer targetVal, Integer type) {
//        switch (type) {
//            case 0:
//                return actVal < targetVal;
//            case 1:
//                return actVal.equals(targetVal);
//            case 2:
//                return actVal > targetVal;
//            default:
//                return false;
//        }
//    }
//
//
//}
