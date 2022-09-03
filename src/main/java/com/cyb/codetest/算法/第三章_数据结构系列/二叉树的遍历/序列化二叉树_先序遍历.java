package com.cyb.codetest.算法.第三章_数据结构系列.二叉树的遍历;

import com.cyb.codetest.算法.第三章_数据结构系列.二叉搜索树.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 剑指 Offer 37. 序列化二叉树
 * 297. 二叉树的序列化与反序列化
 *
 * @author cyb
 * @date 2022/9/1 上午10:17
 */
public class 序列化二叉树_先序遍历 {


    StringBuilder sb = new StringBuilder();

    String dou = ",";


    /**
     * 先序遍历
     * @param root
     * @return
     */
    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        if (root == null) {
            return "null";
        }
        sb.append(root.val).append(dou);
        serialize(root.left);
        serialize(root.right);
        System.out.println(sb.toString());
        return sb.toString();
    }


    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        LinkedList<String> list = new LinkedList<>();

        String[] split = data.split(dou);
        for (int i = 0; i < split.length; i++) {
            list.add(split[i]);
        }

        return deserialize(list);
    }

    public TreeNode deserialize(LinkedList<String> list) {

        if (list.isEmpty()) {
            return null;
        }

        //前序遍历第一个节点就是root节点
        String rootStr = list.removeFirst();

        if (rootStr == "null") {
            return null;
        }

        TreeNode root = new TreeNode(Integer.parseInt(rootStr));

        //求root的左子树 右子树
        root.left = deserialize(list);
        root.right = deserialize(list);

        return root;
    }

}
