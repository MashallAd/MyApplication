package com.example.zx.myapplication;

/**
 * Created by zx on 2015/9/23.
 */
public class StringMatcher {

    /**
     *
     * @param value item的文本内容
     * @param keyword 索引的列表内容
     * @return
     */
    public static boolean match(String value, String keyword) {
        if (value == null || keyword == null) {
            return false;
        }
        if (keyword.length() > value.length()) {
            return  false;
        }
        if (value.contains(keyword)) {
            return true;
        } else {
            return false;
        }
    }
}
