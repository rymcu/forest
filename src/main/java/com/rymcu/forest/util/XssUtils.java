package com.rymcu.forest.util;

import cn.hutool.http.HtmlUtil;
import org.apache.commons.lang.StringUtils;

/**
 * Created on 2022/5/10 17:06.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @packageName com.rymcu.forest.util
 */
public class XssUtils {

    /**
     * 滤除content中的危险 HTML 代码, 主要是脚本代码, 滚动字幕代码以及脚本事件处理代码
     * @param content 需要滤除的字符串
     * @return 过滤的结果
     */
    public static String replaceHtmlCode(String content) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        if (0 == content.length()) {
            return "";
        }
        // 需要滤除的脚本事件关键字
        String[] eventKeywords = {
                "onmouseover", "onmouseout", "onmousedown", "onmouseup", "onmousemove", "onclick", "ondblclick",
                "onkeypress", "onkeydown", "onkeyup", "ondragstart", "onerrorupdate", "onhelp", "onreadystatechange",
                "onrowenter", "onrowexit", "onselectstart", "onload", "onunload", "onbeforeunload", "onblur",
                "onerror", "onfocus", "onresize", "onscroll", "oncontextmenu", "alert"
        };
//        content = replace(content, "<script", "<script", false);
//        content = replace(content, "</script", "</script", false);
//        content = replace(content, "<marquee", "<marquee", false);
//        content = replace(content, "</marquee", "</marquee", false);
        content = HtmlUtil.removeHtmlTag(content, "script");
        content = HtmlUtil.removeHtmlTag(content, "marquee");
        // 将单引号替换成下划线
//        content = replace(content, "'", "_", false);
        // 将双引号替换成下划线
//        content = replace(content, "\"", "_", false);
        // 滤除脚本事件代码
        for (int i = 0; i < eventKeywords.length; i++) {
            // 去除相关属性
            content = HtmlUtil.removeHtmlAttr(content, eventKeywords[i]);
        }
        return content;
    }

    /**
     * 将字符串 source 中的 oldStr 替换为 newStr, 并以大小写敏感方式进行查找
     *
     * @param source 需要替换的源字符串
     * @param oldStr 需要被替换的老字符串
     * @param newStr 替换为的新字符串
     */
    private static String replace(String source, String oldStr, String newStr) {
        return replace(source, oldStr, newStr, true);
    }

    /**
     * 将字符串 source 中的 oldStr 替换为 newStr, matchCase 为是否设置大小写敏感查找
     *
     * @param source    需要替换的源字符串
     * @param oldStr    需要被替换的老字符串
     * @param newStr    替换为的新字符串
     * @param matchCase 是否需要按照大小写敏感方式查找
     */
    private static String replace(String source, String oldStr, String newStr,boolean matchCase) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        // 首先检查旧字符串是否存在, 不存在就不进行替换
        if (!source.toLowerCase().contains(oldStr.toLowerCase())) {
            return source;
        }
        int findStartPos = 0;
        int a = 0;
        while (a > -1) {
            int b = 0;
            String str1, str2, str3, str4, strA, strB;
            str1 = source;
            str2 = str1.toLowerCase();
            str3 = oldStr;
            str4 = str3.toLowerCase();
            if (matchCase) {
                strA = str1;
                strB = str3;
            } else {
                strA = str2;
                strB = str4;
            }
            a = strA.indexOf(strB, findStartPos);
            if (a > -1) {
                b = oldStr.length();
                findStartPos = a + b;
                StringBuilder stringBuilder = new StringBuilder(source);
                source = stringBuilder.replace(a, a + b, newStr) + "";
                // 新的查找开始点位于替换后的字符串的结尾
                findStartPos = findStartPos + newStr.length() - b;
            }
        }
        return source;
    }

}


