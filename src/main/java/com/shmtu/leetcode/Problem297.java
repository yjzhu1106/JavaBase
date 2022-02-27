package com.shmtu.leetcode;


import com.shmtu.bean.TreeNode;

import java.util.LinkedList;

public class Problem297 {


    // 代表分隔符的字符
    String SEP = ",";
    // 代表 null 空指针的字符
    String NULL = "null";


    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        if (root == null) {
            return "";
        }
        StringBuilder res = new StringBuilder();
        serialize(root, res);
        return res.toString();
    }

    private void serialize(TreeNode root, StringBuilder res) {
        if (root == null) {
            res.append(NULL).append(SEP);
            return;
        }

        res.append(root.val).append(SEP);

        serialize(root.left, res);
        serialize(root.right, res);
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        LinkedList<String> nodes = new LinkedList<>();
        for (String s : data.split(SEP)) {
            nodes.addLast(s);
        }
        return deserialize(nodes);

    }

    /* 辅助函数，通过 nodes 列表构造二叉树 */
    private TreeNode deserialize(LinkedList<String> nodes) {
        if (nodes.isEmpty()) return null;

        /****** 前序遍历位置 ******/
        // 列表最左侧就是根节点
        String first = nodes.removeFirst();
        if (first.equals(NULL) || first.equals("")) return null;
        TreeNode root = new TreeNode(Integer.parseInt(first));
        /***********************/

        root.left = deserialize(nodes);
        root.right = deserialize(nodes);

        return root;
    }


}
