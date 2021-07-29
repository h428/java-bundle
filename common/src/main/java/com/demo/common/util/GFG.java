package com.demo.common.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// 基于 Java 的 SHA 算法

public class GFG {

    /**
     * 给定输入计算 SHA
     *
     * @param input
     * @return
     */
    public static String getSHA256(String input) {
        try {
            // 构造基于 SHA256 的实例
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // 对输入进行 hash 得到字节数组 256 bit = 32 字节
            byte[] messageDigest = md.digest(input.getBytes());

            // 将字节数组转化为对应的大正整数
            BigInteger no = new BigInteger(1, messageDigest);

            // 然后转化为 16 进制，256 bit = 64 个十六进制字符
            String hashtext = no.toString(16);

            // 不够 64 个则补充前导 0
            while (hashtext.length() < 64) {
                hashtext = "0" + hashtext;
            }

            // 最终返回字符串形式的 SHA256 哈希值
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown"
                    + " for incorrect algorithm: " + e);
            return null;
        }
    }

    // 可以使用 mysql : `select sha2('GeeksForGeeks', 256);` 测试 SHA256
    public static void main(String args[]) throws NoSuchAlgorithmException {
        System.out.println("测试 SHA-256 生成:");

        // GeeksForGeeks -> 0112e476505aab51b05aeb2246c02a11df03e1187e886f7c55d4e9935c290ade
        String s1 = "GeeksForGeeks";
        System.out.println("\n" + s1 + " : " + getSHA256(s1));

        // 10091009 -> f10c0522657093e416c1b685174cfa2d2a5051440c7fe89a2f4a7de2fc10e6f7
        String s2 = "1009" + "1009";
        System.out.println("\n" + s2 + " : " + getSHA256(s2));
    }
} 