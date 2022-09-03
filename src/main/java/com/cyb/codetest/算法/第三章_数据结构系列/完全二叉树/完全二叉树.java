package com.cyb.codetest.算法.第三章_数据结构系列.完全二叉树;

import com.cyb.codetest.算法.第三章_数据结构系列.二叉搜索树.TreeNode;

/**
 * @author cyb
 * @date 2022/9/1 上午10:00
 */
public class 完全二叉树 {


    /*
     * 求一颗完全二叉树的节点数
     *
     */
    public int countNodes(TreeNode root) {

        //1 计算左右子树的高度
        TreeNode rightTree = root;
        TreeNode leftTree = root;
        int lh = 0, rh = 0;


        while (leftTree != null) {
            lh++;
            leftTree = leftTree.left;

        }
        while (rightTree != null) {
            rh++;
            rightTree = rightTree.left;

        }

        //2 如果左右子树高度一致 那么就是一个满二叉树
        if(lh == rh){
            return (int)Math.pow(2,lh) - 1;
        }

        //3 不一致就是完全二叉树
        return 1+countNodes(root.left) + countNodes(root.right);


    }
}
