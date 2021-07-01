package com.cyb.codetest.算法;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author cyb
 * @date 2021/1/18 11:04 上午
 */
public class 是否是对称二叉树 {


    public boolean isSymmetric(TreeNode root) {

        if (root == null) {
            return true;
        }
        if (root.left == null && root.right == null) {
            return true;
        }
        if ((root.left == null && root.right != null)
                || (root.left != null && root.right == null)) {
            return false;
        }

        TreeNode left = null;
        TreeNode right = null;
        if (root.left != null && root.right != null) {
            left = root.left;
            right = root.right;
            if (left.val != right.val) {
                return false;
            }
        }
        List<Integer> listLeft = new ArrayList<>();
        List<Integer> listRight = new ArrayList<>();
        Stack<TreeNode> stackLeft = new Stack<>();
        Stack<TreeNode> stackRight = new Stack<>();
        stackLeft.push(left);
        System.out.println(left);
        listLeft.add(left.val);
        while (!stackLeft.isEmpty()) {
            TreeNode pop = stackLeft.pop();

            if (pop.left != null) {
                stackLeft.add(pop.left);
                listLeft.add(pop.left.val);
            } else {
                listLeft.add(-1);
            }
            if (pop.right != null) {
                stackLeft.add(pop.right);
                listLeft.add(pop.right.val);
            } else {
                listLeft.add(-1);
            }
        }
        stackRight.push(right);
        listRight.add(right.val);
        while (!stackRight.isEmpty()) {
            TreeNode pop = stackRight.pop();

            if (pop.right != null) {
                stackRight.add(pop.right);
                listRight.add(pop.right.val);
            } else {
                listRight.add(-1);
            }
            if (pop.left != null) {
                stackRight.add(pop.left);
                listRight.add(pop.left.val);
            } else {
                listRight.add(-1);
            }

        }
        System.out.println(listLeft);
        System.out.println(listRight);

        if (listLeft.size() != listRight.size()) {
            return false;
        }
        for (int i = 0; i < listLeft.size(); i++) {
            if (listLeft.get(i) != listRight.get(i)) {
                return false;
            }
        }

        return true;
    }
}

@Data
class TreeNode {
    int val;
    public TreeNode left;
    public TreeNode right;

    TreeNode(int x) {
        val = x;
    }

}
