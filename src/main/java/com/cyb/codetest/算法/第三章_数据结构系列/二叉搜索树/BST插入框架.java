package com.cyb.codetest.算法.第三章_数据结构系列.二叉搜索树;

/**
 *
 * 701. 二叉搜索树中的插入操作
 *
 * @author cyb
 * @date 2022/8/30 下午2:20
 */
public class BST插入框架 {


    /**
     * 将val插入BST 并且返回插入后二叉搜索树的根节点
     * @param root
     * @param val
     * @return
     */
    TreeNode insertIntoBST(TreeNode root,int val){


        if(root == null){
            return new TreeNode(val);
        }

        if(root.val == val){
            return root;
        }

        //插到左子树上
        if(val < root.val){
            root.left = insertIntoBST(root.left, val);
        }

        //插到右子树上
        if(val > root.val){
            root.right = insertIntoBST(root.right, val);
        }

        return root;

    }
}
