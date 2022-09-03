package com.cyb.codetest.算法.第三章_数据结构系列.二叉树的遍历;

import com.cyb.codetest.算法.第三章_数据结构系列.二叉搜索树.TreeNode;

import java.util.LinkedList;

/**
 * @author cyb
 * @date 2022/9/1 上午11:10
 */
public class 序列化二叉树_后序遍历 {


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

        serialize(root.left);
        serialize(root.right);
        sb.append(root.val).append(dou);
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
        String rootStr = list.removeLast();

        if (rootStr == "null") {
            return null;
        }

        TreeNode root = new TreeNode(Integer.parseInt(rootStr));

        //求root的右子树 左子树
        root.right = deserialize(list);
        root.left = deserialize(list);


        return root;
    }
}
