package com.demo.shiro;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @author jingLv
 * @date 2020/09/23
 */
public class TestShiroMD5 {
    public static void main(String[] args) {
        //创建一个MD5算法
/*        Md5Hash md5Hash = new Md5Hash();

        md5Hash.setBytes("123".getBytes());

        String s = md5Hash.toHex();
        System.out.println(s);*/

        // 使用MD5
        Md5Hash md5Hash = new Md5Hash("123");
        System.out.println(md5Hash.toHex());

        // 使用MD5 + Salt处理
        Md5Hash md5Hash1 = new Md5Hash("123", "X0*7ps");
        System.out.println(md5Hash1.toHex());

        // 使用MD5 + salt + hash散列
        Md5Hash md5Hash2 = new Md5Hash("123", "X0*7ps", 1024);
        System.out.println(md5Hash2.toHex());
    }
}
