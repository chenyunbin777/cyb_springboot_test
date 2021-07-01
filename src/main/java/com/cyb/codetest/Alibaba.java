package com.cyb.codetest;

import java.util.*;

/**
 * @author cyb
 * @date 2021/1/18 9:51 下午
 */
public class Alibaba {

    public static class Store {
        long priority;//值越小，优先级越高
        long storeId;
    }

    public static class Item {
        int itemId;
    }

    /**
     * 店铺是否包含该商品
     *
     * @param item
     * @param store
     * @return
     */
    public boolean contains(Item item, Store store) {
        return true;
    }

    public Store getPriorityStore(List<Item> items, List<Store> stores) {

        if (Objects.isNull(items) || Objects.isNull(stores)) {
            return null;
        }
        Store storeResult = null;

        long priority = Integer.MAX_VALUE;
        boolean flag = true;
        for (int i = 0; i < stores.size(); i++) {
            Store store = stores.get(i);
            for (int j = 0; j < items.size(); j++) {
                if (!contains(items.get(j), store)) {
                    flag = false;
                }
            }
            if (flag && store.priority < priority) {
                priority = store.priority;
                storeResult = store;
            }
        }

        return storeResult;
    }


    /**
     * 最少的店铺找到匹配的商品，跟优先级无关
     * 一定包含所有商品
     *
     * @param items
     * @param stores
     * @return
     */
    public Map<Item, Store> getMap(List<Item> items, List<Store> stores) {
        if (Objects.isNull(items) || Objects.isNull(stores)) {
            return null;
        }
        Map<Item, Store> resultMap = new HashMap<>();
        Store priorityStore = getPriorityStore(items, stores);
        if (Objects.nonNull(priorityStore)) {
            for (int j = 0; j < items.size(); j++) {
                resultMap.put(items.get(j), priorityStore);
            }
        }

        TreeMap<Item, Integer> mapCount = new TreeMap<>(new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return 0;
            }
        });

        int count = 0;
        Store storeResult = null;
        List<Item> collectList = new ArrayList<>();
        List<Item> remainList = new ArrayList<>();
        List<Store> storeList = new ArrayList<>();

        HashMap<Store, List<Item>> map = new HashMap<>();
        while (!remainList.isEmpty()) {

            for (int i = 0; i < stores.size(); i++) {
                Store store = stores.get(i);
                int itemCount = 0;
                List<Item> collectListTemp = new ArrayList<>();
                List<Item> remainListTemp = new ArrayList<>();
                for (int j = 0; j < items.size(); j++) {
                    Item item = items.get(j);

                    if (contains(item, store)) {
                        collectListTemp.add(item);
                        itemCount++;
                    } else {
                        remainListTemp.add(item);
                    }
                }


                //找到商品最多的门店
                if (itemCount > count) {
                    count = itemCount;
                    storeResult = store;
                    remainList.addAll(remainListTemp);
                    remainList.clear();
                    collectList.addAll(collectListTemp);
                    collectListTemp.clear();
                }
            }
            map.put(storeResult, collectList);
//            storeList.add(storeResult);
        }


        return resultMap;
    }

}
