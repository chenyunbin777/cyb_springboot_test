//package com.cyb.codetest;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.*;
//
///**
// * //评测题目: 模拟门店分单
// * //输入：商品列表、可选门店列表
// * //输出：每个商品分配到哪个门店
// * //要求：先实现方法findOneStore，即，查找出所有商品都有库存的最优先的一个门店，如果找不到，返回null
// * //再实现方法findStores，即，使用最少的门店完成所有商品到门店的匹配
// * //业务里确认每个商品一定能找到一个有货门店
// * //请按照生产标准进行代码书写，考虑代码的可维护性
// *
// * @author cyb
// * @date 2021/1/18 10:58 下午
// */
//public class InvFinder {
//    // 商品类
//    public static class Item {
//        long itemId;
//    }
//
//    // 门店类
//    public static class Store {
//        long storeId;
//        long priority; // 越小越优先
//    }
//
//    // 库存查询能力
//    interface InvManager {
//        boolean hasInv(Store store, Item item);
//    }
//
//    // 库存查询manager bean
//    private InvManager invManager;
//
//    // log
//    private static final Logger log = LoggerFactory.getLogger("InvFinderLog");
//
//    /**
//     * 查找出所有商品都有库存的最优先的一个门店，如果找不到，返回null
//     * items：所有的商品信息
//     * stores：所有的门店
//     */
//    public Store findOneStore(List<Item> items, List<Store> stores) {
//
//        if (Objects.isNull(items) || Objects.isNull(stores)) {
//            return null;
//        }
//
//        // 从这里开始实现
//        //如果找到一个优先级最高的门店flag = true
//        Store storeResult = null;
//        long priority = Long.MAX_VALUE;
//        boolean flag = true;
//        for (int j = 0; j < stores.size(); j++) {
//            Store store = stores.get(j);
//            for (int i = 0; i < items.size(); i++) {
//                if (!invManager.hasInv(store, items.get(i))) {
//                    flag = false;
//                    break;
//                }
//            }
//            if (flag && store.priority < priority) {
//                priority = store.priority;
//                storeResult = store;
//            }
//        }
//
//        return storeResult;
//    }
//
//    /**
//     * 再实现方法findStores，即，使用最少的门店完成所有商品到门店的匹配
//     * items：所有的商品信息
//     * stores：所有的门店
//     */
//    public Map<Item, Store> findStores(List<Item> items, List<Store> stores) {
//        Map<Item, Store> resultMap = new HashMap<Item, Store>();
//        // 从这里开始实现
//        Store storeResult = findOneStore(items, stores);
//        //找到了所有商品都有的门店
//        if (Objects.nonNull(storeResult)) {
//            log.info("找到了唯一门店:{}", storeResult.storeId);
//            for (int i = 0; i < items.size(); i++) {
//                resultMap.put(items.get(i), storeResult);
//                return resultMap;
//            }
//        }
//
//
//        HashMap<Store, List<Item>> map = new HashMap<>();
//
//        //每次找到一个商品最多的店铺
////        Store maxStore = null;
////        for (Item item : items) {
////            for (Store store:stores){
////
////                if(invManager.hasInv(store, item)){
////                    if(map.containsKey(store)){
////                        map.get(store).add(item);
////                    }else {
////                        List<Item> list = new ArrayList<>();
////                        list.add(item);
////                        map.put(store,list);
////                    }
////
////                    maxStore
////                }
////
////            }
////
////        }
//        //贪心算法：每次找到一个商品最多的店铺，直到商品被选择完
//        int max = 0;
//        List<Item> storeItemsMax = new ArrayList<>();
//        while (!items.isEmpty()) {
//            List<Item> storeItemsMaxTemp = new ArrayList<>();
//            Store storeRes = null;
//            for (Store store : stores) {
//                List<Item> storeItems = getStoreItems(store, items);
//                if (storeItems.size() > max) {
//                    max = storeItems.size();
//                    storeItemsMaxTemp.clear();
//                    storeItemsMaxTemp.addAll(storeItems);
//                    storeRes = store;
//                }
//            }
//            storeItemsMax.addAll(storeItemsMaxTemp);
//
//            for (int i = 0; i < storeItemsMaxTemp.size(); i++) {
//                resultMap.put(storeItemsMaxTemp.get(i), storeRes);
//            }
//
//            //删除道具，店铺
//            items.removeAll(storeItemsMaxTemp);
//            stores.remove(storeRes);
//
//
//            max = 0;
//        }
//
//
//        return resultMap;
//
//    }
//
//    public static void main(String[] args) {
//    }
//
//
//    /**
//     * @param Store
//     * @param items
//     * @return
//     */
//    public List<Item> getStoreItems(Store Store, List<Item> items) {
//        List<Item> result = new ArrayList<>();
//        for (Item item : items) {
//            if (invManager.hasInv(Store, item)) {
//                result.add(item);
//            }
//        }
//        return null;
//    }
//}
