package com.cyb.codetest.算法.日常刷题;

import com.alibaba.fastjson.JSON;

import java.util.*;

/**
 * @author cyb
 * @date 2022/9/3 下午12:12
 */
public class 最长数对链646 {

    public static void main(String[] args) {
        int[][] pairs = {{2,3},{1,2},  {3,4}};
        System.out.println(JSON.toJSONString(pairs));
        Arrays.sort(pairs, (a, b) -> a[1] - b[1]);
        System.out.println(JSON.toJSONString(pairs));
        findLongestChain(pairs);
    }

    /**
     * 贪心算法
     * 思路
     *
     * 要挑选最长数对链的第一个数对时，最优的选择是挑选第二个数字最小的，这样能给挑选后续的数对留下更多的空间。
     * 挑完第一个数对后，要挑第二个数对时，也是按照相同的思路，是在剩下的数对中，第一个数字满足题意的条件下，
     * 挑选第二个数字最小的。按照这样的思路，可以先将输入按照第二个数字排序，然后不停地判断第一个数字是否能满足
     * 大于前一个数对的第二个数字即可。
     *
     * @param pairs
     * @return
     */
    public int findLongestChainOffical(int[][] pairs) {
        int curr = Integer.MIN_VALUE, res = 0;
        Arrays.sort(pairs, (a, b) -> a[1] - b[1]);
        for (int[] p : pairs) {
            if (curr < p[0]) {
                curr = p[1];
                res++;
            }
        }
        return res;
    }

    public static int findLongestChain(int[][] pairs) {


        HashMap<Integer, Set<Integer>> mapX = new HashMap<>();
        HashMap<Integer, Set<Integer>> mapY = new HashMap<>();
        int maxX = 0;
        int maxY = 0;

        for (int i = 0; i < pairs.length; i++) {
            for (int j = 0; j < pairs[i].length; j++) {
                System.out.println(pairs[i][j]);

                if(j == 0){
                    mapX.putIfAbsent(pairs[i][j],new HashSet<>());
                    mapX.get(pairs[i][j]).add(pairs[i][j]);
                }

            }
        }

        return 0;

    }
}
