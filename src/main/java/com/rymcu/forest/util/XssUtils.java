package com.rymcu.forest.util;

import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HtmlUtil;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

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
        content = HtmlUtil.removeHtmlTag(content, "script");
        content = HtmlUtil.removeHtmlTag(content, "marquee");
        // 滤除脚本事件代码
        for (int i = 0; i < eventKeywords.length; i++) {
            // 去除相关属性
            content = HtmlUtil.removeHtmlAttr(content, eventKeywords[i]);
        }
        return content;
    }

    public static String filterHtmlCode(String content) {
        String regex = "<pre>[\\s|\\S]+?</pre>";
        // 拿到匹配的pre标签List
        List<String> resultFindAll = ReUtil.findAll(regex, content, 0, new ArrayList<>());
        String result = "";
        // size大于0，就做替换
        if (resultFindAll.size() > 0) {
            // 生成一个待替换唯一字符串
            String preTagReplace = UUID.randomUUID().toString() + System.currentTimeMillis();
            // 判断替换字符串是否唯一
            while (ReUtil.findAll(preTagReplace, content, 0, new ArrayList<>()).size() > 0) {
                preTagReplace = UUID.randomUUID().toString() + System.currentTimeMillis();
            }
            Pattern pattern = Pattern.compile(preTagReplace);
            // 替换pre标签内容
            String preFilter = ReUtil.replaceAll(content, regex, preTagReplace);
            System.err.println("pre标签替换");
            System.err.println(preFilter);
            final String[] filterResult = {HtmlUtil.filter(preFilter)};
            resultFindAll.forEach(obj -> {
                filterResult[0] = ReUtil.replaceFirst(pattern, filterResult[0], obj);
            });
            result = filterResult[0];
        } else {
            result = HtmlUtil.filter(content);
        }
        return result;
    }

}


