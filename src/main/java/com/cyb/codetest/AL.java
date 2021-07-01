///**
// * Alipay.com Inc.
// * Copyright (c) 2004-2019 All Rights Reserved.
// */
//
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.mos.api.SkuReadService;
//import com.alibaba.mos.data.ChannelInventoryDO;
//import com.alibaba.mos.data.SkuDO;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Service;
//
//import java.io.*;
//import java.math.BigDecimal;
//import java.util.*;
//
///**
// * TODO: 实现
// *
// * @author superchao
// * @version $Id: SkuReadServiceImpl.java, v 0.1 2019年10月28日 10:49 AM superchao Exp $
// */
//@Service
//@Slf4j
//public class SkuReadServiceImpl implements SkuReadService {
//    final String fileName = "/Users/chenyunbin/Downloads/mos-interview/src/main/resources/data/skus.txt";
//
//    /**
//     * 假设excel数据量很大无法一次性加载到内存中
//     *
//     * @param handler
//     */
//    @Override
//    public void loadSkus(SkuHandler handler) {
//        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
//            String line;
//            while (Objects.nonNull(line = br.readLine())) {
//                //去除掉第一行
//                if (line.contains("id|name|artNo|spuId|skuType|price|inventorys")) {
//                    continue;
//                }
//                // 行数据转换成数组
//                String[] lineArray = line.split("\\|");
//                SkuDO skuDO = SkuDO.builder()
//                        .id(StringUtils.isNotBlank(lineArray[0]) ? lineArray[0] : "")
//                        .name(StringUtils.isNotBlank(lineArray[1]) ? lineArray[1] : "")
//                        .artNo(StringUtils.isNotBlank(lineArray[2]) ? lineArray[2] : "")
//                        .spuId(StringUtils.isNotBlank(lineArray[3]) ? lineArray[3] : "")
//                        .skuType(StringUtils.isNotBlank(lineArray[4]) ? lineArray[4] : "")
//                        .price(StringUtils.isNotBlank(lineArray[5]) ? new BigDecimal(lineArray[5]) : new BigDecimal(0))
//                        .inventoryList(StringUtils.isNotBlank(lineArray[6]) ? JSONObject.parseArray(lineArray[6], ChannelInventoryDO.class) : new ArrayList<>())
//                        .build();
//                handler.handleSku(skuDO);
//            }
//        } catch (FileNotFoundException e) {
//            log.error("文件没有找到", e);
//        } catch (IOException e) {
//            log.error("io异常", e);
//        }
//
//    }
//
//    @Override
//    public String getMedianSkuId() {
//        //价格与skuId的映射关系
//        Map<BigDecimal, String> map = new HashMap<>();
//        //价格
//        List<BigDecimal> priceList = new ArrayList<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
//            String line;
//            while (Objects.nonNull(line = br.readLine())) {
//                //去除掉第一行
//                if (line.contains("id|name|artNo|spuId|skuType|price|inventorys")) {
//                    continue;
//                }
//                // 行数据转换成数组
//                String[] lineArray = line.split("\\|");
//                if (StringUtils.isBlank(lineArray[5])) {
//                    continue;
//                }
//                BigDecimal price = new BigDecimal(lineArray[5]);
//                String skuId = lineArray[0];
//                if (!map.containsKey(price)) {
//                    map.put(price, skuId);
//                    priceList.add(price);
//                }
//
//            }
//        } catch (FileNotFoundException e) {
//            log.error("文件没有找到", e);
//        } catch (IOException e) {
//            log.error("io异常", e);
//        }
//
//        //对价格进行排序
//        Collections.sort(priceList);
//        //说明：中位数与list.size()的奇偶有关系
//        BigDecimal medianPrice = priceList.size() % 2 != 0 ? priceList.get(priceList.size() / 2) : priceList.get(priceList.size() / 2 - 1);
//        return map.get(medianPrice);
//    }
//
//    @Override
//    public BigDecimal getMiddlePrice() {
//        BigDecimal middlePrice = new BigDecimal(0);
//        //以价格为下标，存储每个价格的个数
//        int[] priceArray = new int[10000];
//        //一共有多少个sku
//        int lineCount = 0;
//        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
//            String line;
//            while (Objects.nonNull(line = br.readLine())) {
//                //去除掉第一行
//                if (line.contains("id|name|artNo|spuId|skuType|price|inventorys")) {
//                    continue;
//                }
//                lineCount++;
//                // 行数据转换成数组
//                String[] lineArray = line.split("\\|");
//                if (StringUtils.isBlank(lineArray[5])) {
//                    continue;
//                }
//                BigDecimal price = new BigDecimal(lineArray[5]);
//                //当前price的数量加1
//                priceArray[price.intValue()] = priceArray[price.intValue()] + 1;
//
//            }
//        } catch (FileNotFoundException e) {
//            log.error("文件没有找到", e);
//        } catch (IOException e) {
//            log.error("io异常", e);
//        }
//
//        //priceArray中第几个数为中间的价格
//        int order = lineCount % 2 != 0 ? lineCount / 2 + 1 : lineCount / 2;
//
//        //价格个数
//        int priceCount = 0;
//        for (int i = 0; i < priceArray.length; i++) {
//            //排除无价格
//            if (priceArray[i] == 0) {
//                continue;
//            }
//            priceCount += priceArray[i];
//            //如果count超过了order，也就定位到了中间价格
//            if (priceCount >= order) {
//                middlePrice = new BigDecimal(i);
//                break;
//            }
//
//        }
//
//        return middlePrice;
//    }
//
//    @Override
//    public Map<String, List<String>> getTop5SkuIdList() {
//
//        //key：channel  value：数量@skuId的拼装的list
//        Map<String, List<String>> map = new HashMap<>();
//
//        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
//            String line;
//            while (Objects.nonNull(line = br.readLine())) {
//                //去除掉第一行
//                if (line.contains("id|name|artNo|spuId|skuType|price|inventorys")) {
//                    continue;
//                }
//                // 行数据转换成数组
//                String[] lineArray = line.split("\\|");
//                String skuId = lineArray[0];
//                if (StringUtils.isBlank(skuId)) {
//                    continue;
//                }
//
//                List<ChannelInventoryDO> channelInventoryList = JSONObject.parseArray(lineArray[6], ChannelInventoryDO.class);
//                for (ChannelInventoryDO channelInventoryDO : channelInventoryList) {
//                    String channelCode = channelInventoryDO.getChannelCode();
//                    if (StringUtils.isBlank(channelCode)) {
//                        continue;
//                    }
//                    BigDecimal inventory = channelInventoryDO.getInventory();
//                    if (Objects.isNull(inventory)) {
//                        inventory = new BigDecimal(0);
//                    }
//                    if (!map.containsKey(channelCode)) {
//                        List<String> list = new ArrayList<>();
//                        list.add(inventory + "@" + skuId);
//                        map.put(channelCode, list);
//                    } else {
//                        List<String> countSkuIdList = map.get(channelCode);
//                        countSkuIdList.add(inventory + "@" + skuId);
//
//                        //防止读入内存数据过多，每次countSkuIdList超过5的时候进行重新排序，之后进行截取
//                        if (countSkuIdList.size() > 5) {
//                            Collections.sort(countSkuIdList, new Comparator<String>() {
//                                @Override
//                                public int compare(String str1, String str2) {
//
//                                    String[] split1 = str1.split("@");
//                                    String[] split2 = str2.split("@");
//                                    int inventory1 = Integer.valueOf(split1[0]);
//                                    int inventory2 = Integer.valueOf(split2[0]);
//                                    if (inventory1 - inventory2 > 0) {
//                                        return -1;
//                                    } else if (inventory1 - inventory2 < 0) {
//                                        return 1;
//                                    }
//                                    return 0; //相等为0
//                                }
//                            });
//                            //截取countSkuIdList前5的数据
//                            countSkuIdList = countSkuIdList.subList(0, 5);
//                        }
//                        map.put(channelCode, countSkuIdList);
//
//                    }
//                }
//            }
//        } catch (FileNotFoundException e) {
//            log.error("文件没有找到", e);
//        } catch (IOException e) {
//            log.error("io异常", e);
//        }
//
//        //重新解析map，将skuId分离放入result Map中
//        Map<String, List<String>> result = new HashMap<>();
//        for (String channel : map.keySet()) {
//            List<String> countSkuIdList = map.get(channel);
//            List<String> skuIdList = new ArrayList<>();
//            for (String str : countSkuIdList) {
//                String[] splitArray = str.split("@");
//                skuIdList.add(splitArray[0]);
//            }
//            result.put(channel, skuIdList);
//        }
//
//        return result;
//    }
//
//    @Override
//    public BigDecimal getAllSkuValue() {
//        //所有sku的总价值
//        BigDecimal totalPrice = new BigDecimal(0);
//        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
//            String line;
//            while (Objects.nonNull(line = br.readLine())) {
//                //去除掉第一行
//                if (line.contains("id|name|artNo|spuId|skuType|price|inventorys")) {
//                    continue;
//                }
//                // 行数据转换成数组
//                String[] lineArray = line.split("\\|");
//                if (StringUtils.isBlank(lineArray[5])) {
//                    continue;
//                }
//                int price = Integer.parseInt(lineArray[5]);
//                List<ChannelInventoryDO> channelInventoryList = JSONObject.parseArray(lineArray[6], ChannelInventoryDO.class);
//
//                BigDecimal totalInventory = new BigDecimal(0);
//                for (ChannelInventoryDO channelInventoryDO : channelInventoryList) {
//                    BigDecimal inventory = channelInventoryDO.getInventory();
//                    totalInventory = totalInventory.add(inventory);
//                }
//
//                totalPrice = totalPrice.add(totalInventory.multiply(BigDecimal.valueOf(price)));
//
//            }
//        } catch (FileNotFoundException e) {
//            log.error("文件没有找到", e);
//        } catch (IOException e) {
//            log.error("io异常", e);
//        }
//
//
//        return totalPrice;
//    }
//
//
//}