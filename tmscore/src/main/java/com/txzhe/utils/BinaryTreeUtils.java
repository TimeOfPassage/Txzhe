package com.txzhe.utils;

import com.txzhe.tree.TreeNode;

import java.text.MessageFormat;

public class BinaryTreeUtils {

    private TreeNode root;

    public TreeNode getRoot() {
        return root;
    }

    public TreeNode find(String data){
        return null;
    }

    public void delete(String data){
        TreeNode node = new TreeNode(data);
        if(root == null){
            root = node;
            root.setLeft(null);
            root.setRight(null);
        }else{
            TreeNode curNode = node;
            TreeNode parentNode;
        }
    }

    public static void main(String[] args) {
        String s = "我们是共产主义接班人，{0},{1},{2}";
        System.out.println(MessageFormat.format(s, "1", "2", "3"));
    }
}
