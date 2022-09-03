package com.cyb.codetest.算法.第三章_数据结构系列.二叉搜索树;

/**
 * 98. 验证二叉搜索树
 *
 * 有效 二叉搜索树定义如下：
 *
 * 1 节点的左子树只包含 小于 当前节点的数。
 * 2 节点的右子树只包含 大于 当前节点的数。
 * 3 所有左子树和右子树自身必须也是二叉搜索树。
 * @author cyb
 * @date 2022/8/30 下午2:08
 */
public class isValidBST {

    public boolean isValidBST(TreeNode root) {
        return isValidBST( root, null, null);
    }

    public boolean isValidBST(TreeNode root, TreeNode min, TreeNode max){

        if(root == null) return true;

        if(min !=null && root.val<= min.val) return false;

        if(max !=null && root.val >= max.val) return false;

        return isValidBST(root.left, min, root) && isValidBST(root.right, root, max);

    }

}
