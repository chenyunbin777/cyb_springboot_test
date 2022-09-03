package com.cyb.codetest.算法.第三章_数据结构系列.二叉搜索树;

/**
 *
 * 450. 删除二叉搜索树中的节点
 * @author cyb
 * @date 2022/8/30 下午2:39
 */
public class BST删除框架 {


    TreeNode deleteNode(TreeNode root,int val){


        if(root == null){
            return null;
        }

        if(root.val == val){

            //TODO 删除这个节点
            // 情况1：删除节点没有左右子树
            if(root.left == null && root.right == null){
                return null;
            }

            // 情况2：当前节点只存在左子树 右子树中的一个，使用其子树的各节点替代这个节点
            if (root.left == null && root.right != null) {
                return root.right;
            }

            if (root.left != null && root.right == null) {
                return root.left;
            }


            // 情况3：左右子树都存在。为了不破坏BST的特性，要找到左子树中的最大节点或者右子树最小节点替代它

            // 3-1 找到右子树最小节点
            if (root.left != null && root.right != null) {
                //找到右子树最小节点
                TreeNode rightMinNode = getRightMinNode(root.right);

                //把root修改为minNode
                root.val = rightMinNode.val;

                //删除minNode
                root.right = deleteNode(root.right,rightMinNode.val);
            }



            return root;
        }

        //插到左子树上
        if(val < root.val){
            root.left = deleteNode(root.left, val);
        }

        //插到右子树上
        if(val > root.val){
            root.right = deleteNode(root.right, val);
        }

        return root;

    }


    /**
     * 得到右子树最小节点
     * @param root
     * @return
     */
    TreeNode getRightMinNode(TreeNode root){

        if(root.left != null){
           return getRightMinNode(root.left);
        }
        //如果左子树为null就返回右子树根
        System.out.println("getRightMinNode:"+root.val);
        return root;

    }



}
