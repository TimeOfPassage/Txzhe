package com.txzhe.tree;

public class TreeNode {

    private String data;

    private TreeNode left;

    private TreeNode right;

    public TreeNode(){}

    public TreeNode(TreeNode left, TreeNode right, String data){
        this.data = data;
        this.left = left;
        this.right = right;
    }

    public TreeNode(String data) {
        this(null,null,data);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }
}
