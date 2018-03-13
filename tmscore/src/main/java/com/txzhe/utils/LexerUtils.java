package com.txzhe.utils;

import java.io.*;

//词法分析
public class LexerUtils {
    // 源程序
    private StringBuffer sourceCode = new StringBuffer();
    // 下一个字符所占位置
    private int position = 0;
    // 存放最新的字符
    private char c;
    // 分隔符数组
    private char separators[] = { ',', ';', '{', '}', '(', ')', '[', ']', '_',':', '、', '.', '"','\'' };
    // 运算符数组
    private char operators[] = { '+', '-', '*', '/', '=', '>', '<', '&' };
    // 关键字数组
    private String keyWords[] = { "abstract", "boolean", "break", "byte",
            "case", "catch", "char", "class", "continue", "default", "do",
            "double", "else", "extends", "final", "finally", "float", "for",
            "if", "implements", "import", "instanceof", "int", "interface",
            "long", "native", "new", "package", "private", "protected",
            "public", "return", "short", "static", "super", "switch",
            "synchronized", "this", "throw", "throws", "transient", "try",
            "void", "volatile", "while", "strictfp","enum","goto","const","assert" }; // 关键字数组
    // 种别码 分隔符: [101,150) | 运算符: (51,100) |  常数: 0 | 标识符: 200 | 关键字: [1,50]
    private int ketType;
    // 字符数组，存放构成单词符号的字符串(单词一类)
    private String strToken = "";

    public void lexer() throws IOException {
        scanSourceCode("a.txt");
        /**
         * 标识符定义：字母、数字、下划线
         *
         * 一个字符一个字符读入，然后进行状态判断
         *  1、字母([a-zA-Z])
         *      1、标识符
         *      2、关键字(public\void\class...)
         *  2、数字([0-9])
         *      1、循环检测 11,2123,2313
         *      2、连接字符
         *
         *  3、操作符( + - * / ...)
         *
         *  4、分隔符({}()''"")
         */
        while(position < sourceCode.length()) {
            //获取字符
            c = nextChar();
            //检测字符
            checkCharacter();
            if( isLetter() ) {
                //字母|数字
                while(isDigit() || isLetter()){
                    concat();
                    c = nextChar();
                }
                retract();
                if (isKeyWord()) { // 如果是为关键字，则插入到1.保留字表中
                    handleKeyWords();
                } else { // 否则插入到2.符号表中
                    handleIdentify();
                }
                strToken = "";
            }else if( isDigit() ) {
                while ( isDigit() ){
                    concat();
                    c = nextChar();
                }
                // 123 + 撤销一步（回撤）
                retract();
                //归类
                handleConst();
                //置空字符串
                strToken = "";
            }else if( isOperator() ) {
                handleOperators();
            }else if( isSeparators() ) {
                handleSeparators();
            }
        }
    }
    //----------------------------------工具Method----------------------------------------------//
    //将指示器回调一次,当前字符置为空
    private void retract() {
        position--;
        c = ' ';
    }
    //连接字符
    private void concat() {
        strToken += c;
    }
    //获取下一个字符
    private char nextChar() {
        //System.out.println(String.format("%s:%s",position,sourceCode.charAt(position)));
        return sourceCode.charAt(position++);
    }
    //----------------------------------归类Method----------------------------------------------//
    //归类标识符
    private void handleIdentify() {
        System.out.println(String.format("<%d,%s>",200,strToken));
        //System.out.println(String.format("种别码：%d,对应值：%s --> <%d,%s>",200,strToken,200,strToken));
    }
    //归类关键字
    private void handleKeyWords() {
        System.out.println(String.format("<%d,%s>",ketType,strToken));
        //System.out.println(String.format("种别码：%d,对应值：%s --> <%d,%s>",ketType,strToken,ketType,strToken));
    }
    //归类常数
    private void handleConst() {
        int num = Integer.parseInt(strToken);
        System.out.println(String.format("<%d,%s>",0,num));
        //System.out.println(String.format("种别码：%d,对应值：%s --> <%d,%s>",0,num,0,num));
    }
    //归类操作符
    private void handleOperators() {
        System.out.println(String.format("<%d,%s>",ketType,c));
        //System.out.println(String.format("种别码：%d,对应值：%s --> <%d,%s>",ketType,c,ketType,c));
    }
    //归类分隔符
    private void handleSeparators() {
        System.out.println(String.format("<%d,%s>",ketType,c));
        //System.out.println(String.format("种别码：%d,对应值：%s --> <%d,%s>",ketType,c,ketType,c));
    }
    //----------------------------------检测Method----------------------------------------------//
    //检测是否是关键字
    private boolean isKeyWord() {
        ketType = -1;
        for (int i = 0; i < keyWords.length; i++) {
            if (keyWords[i].equals(strToken)) ketType = i + 1;
        }
        return ketType > 0 ? true :false;
    }
    //检测字符是否为空白字符,如果为空白字符，继续读取，直至非空白字符
    private void checkCharacter() {
        while(Character.isSpaceChar(c))
            c = nextChar();
    }
    //检测是否是分隔符
    private boolean isSeparators() {
        ketType = -1;
        for (int i = 0; i < separators.length; i++) {
            if (c == separators[i]) ketType = i + 101;
        }
        return ketType > 0 ? true :false;
    }

    //检测是否是操作符 + - = /
    private boolean isOperator() {
        ketType = -1;
        for (int i = 0; i < operators.length; i++) {
            if (c == operators[i]) {
                ketType = i + 51;
            }
        }
        return ketType > 0 ? true :false;
    }

    //检测是否字符 a-z A-Z
    private boolean isLetter() {
        return Character.isLetter(c);
    }

    //检测是否是数字 0-9
    private boolean isDigit() {
        return Character.isDigit(c);
    }
    public void scanSourceCode(String fileName) throws IOException {
        if(fileName != null && !"".equals(fileName)){
            String file= LexerUtils.class.getResource("/" + fileName).getPath();
            BufferedReader br = new BufferedReader(new FileReader(new File(file)));
            String line = "";
            while((line = br.readLine()) != null){
                sourceCode.append(line);
            }
            br.close();
        }else{
            throw new RuntimeException(fileName+":不能为空");
        }
    }

    public static void main(String[] args) {
        System.err.println("常数，种别0");
        System.err.println("关键字，种别[1,50]");
        System.err.println("运算符，种别 [51,100)");
        System.err.println("分隔符，种别 [101,150)");
        System.err.println("标识符，种别200");
        LexerUtils l = new LexerUtils();
        try {
            l.lexer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
