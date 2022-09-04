package com.cyb.codetest.算法.日常刷题;

/**
 * 给你一个大小为 rows x cols 的矩阵 mat，其中 mat[i][j] 是 0 或 1，请返回 矩阵 mat 中特殊位置的数目 。
 * <p>
 * 特殊位置 定义：如果 mat[i][j] == 1 并且第 i 行和第 j 列中的所有其他元素均为 0（行和列的下标均 从 0 开始 ），则位置 (i, j) 被称为特殊位置。
 *
 * @author cyb
 * @date 2022/9/4 上午9:22
 */
public class 二进制矩阵中的特殊位置1582 {


    public static void main(String[] args) {
        int[][] mat = {
                        {1,0,0},
                        {0,0,1},
                        {1,0,0}
        };

        System.out.println( numSpecial(mat));
    }

    public static int numSpecial(int[][] mat) {
        int ans = 0;

        for (int i = 0; i < mat.length; i++) {
            int[] matI = mat[i];
            boolean rowFlag = false;
            int rowIndex = -1;
            for (int j = 0; j < matI.length; j++) {
                int matJ = matI[j];
                //说明数组i行上就没有特殊位置了，特殊位置在行或者列上只有一个
                if (!rowFlag && matJ == 1) {
                    System.out.println("rowFlag:"+1111);
                    rowFlag = true;
                    rowIndex = j;
                } else if(rowFlag && matJ == 1){
                    System.out.println("rowFlag:"+2222);
                    rowFlag = false;
                    //如果某一行遇到了多个1 需要结束循环，这一行已经不存在特殊字符了
                    break;
                }
            }
            System.out.println("rowFlag:"+rowFlag);
            //如果当前行不存在特殊位置直接返回
            if (!rowFlag) {
                continue;
            }

            System.out.println(rowIndex);
            boolean columnFlag = true;
            for (int j = 0; j < mat.length; j++) {
                int columnNum = mat[j][rowIndex];
                if(i == j){
                    continue;
                }
                if (columnNum != 0) {
                    columnFlag = false;
                    break;
                }
            }

            System.out.println(columnFlag);
            if(columnFlag){
                ans++;
            }

        }

        return ans;
    }
}
