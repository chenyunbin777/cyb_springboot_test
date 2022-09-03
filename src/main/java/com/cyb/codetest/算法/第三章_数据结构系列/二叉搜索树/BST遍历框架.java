package com.cyb.codetest.算法.第三章_数据结构系列.二叉搜索树;

/**
 *
 * 700. 二叉搜索树中的搜索
 * @author cyb
 * @date 2022/8/30 下午2:12
 */
public class BST遍历框架 {


    /**
     *
     * 查找BST树中是否存在某个元素
     * @param root
     * @param target
     * @return
     */
    Boolean searchBST(TreeNode root, int target){
        if(root.val == target){

            //TODO 这里可以写处理的方式

            return true;
        }
        if(target<root.val){
            return searchBST(root.left,target);
        }
        if(target>root.val){
            return searchBST(root.right,target);
        }

        return false;
    }
}
