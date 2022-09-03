package com.cyb.codetest.算法.第三章_数据结构系列.二叉搜索树;

/**
 * @author cyb
 * @date 2022/8/30 下午2:11
 */
public class TreeNode {

    public int val;
    public TreeNode left;
    public TreeNode right;

    TreeNode() {
    }

    public TreeNode(int val) {
        this.val = val;
    }

    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}
