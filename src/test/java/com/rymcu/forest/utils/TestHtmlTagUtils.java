package com.rymcu.forest.utils;

import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HtmlUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rymcu.forest.util.ContentHtmlTagUtils.replacePreTagAndFilterXss;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * html.preTag.replace
 */
public class TestHtmlTagUtils {

    /**
     * 过滤HTML文本，防止XSS攻击
     */
    @Test
    public void testFilter() {
        String html = "<img src='' javascript=''>";
        // 结果为：""
        String result = HtmlUtil.filter(html);

        System.out.println(result);
    }

    /**
     * 找到所有
     */
    @Test
    public void testFindAll() {
        String content = "ZZZaaabbbccc中文1234";
        List<String> resultFindAll = ReUtil.findAll("\\w{2}", content, 0, new ArrayList<>());
        System.out.println(resultFindAll);
    }

    /**
     * uuid
     */
    @Test
    public void testUUID() {
        System.out.println(UUID.randomUUID().toString() + System.currentTimeMillis());
    }

    /**
     * 找到所有pre标签
     */
    @Test
    public void testFindPreTag() {
        String regex = "<pre>[\\s|\\S]+?</pre>";
        String content = "<pre>\n" + "123" + "</pre><pre>\n" + "3333333" + "</pre><pre>\n" + "55555555555" + "</pre><pre>\n" + "4555555" + "</pre><pre>\n" + "99999999999" + "</pre>sdfsdf</pre>";
        List<String> resultFindAll = ReUtil.findAll(regex, content, 0, new ArrayList<>());
        System.out.println(resultFindAll.size());
        System.out.println(resultFindAll);

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

    /**
     * 拦截xxs前先把pre标签对给提取置换再拦截xxs，然后再把pre标签换回来
     */
    @Test
    public void testReplaceContent() {
        String regex = "(<pre>[\\s|\\S]+?</pre>)|(<code>[\\s|\\S]+?</code>)";
        String content = "<img src='' javascript='11'><img src='' onerror='alert'><code>\n" + "123<img src='' javascript=''><img src='' onerror=''>" + "</code><img src='' javascript='11231231'><img src='' onerror='alert785'><pre>\n" + "3333333<img src='' javascript=''><img src='' onerror=''>" + "</pre><pre>\n" + "55555555555<img src='' javascript=''><img src='' onerror=''>" + "</pre><pre>\n" + "4555555<img src='' javascript=''><img src='' onerror=''>" + "</pre><pre>\n" + "99999999999<img src='' javascript=''><img src='' onerror=''>" + "</pre>sdfsdf<img src='' javascript=''><img src='' onerror=''></pre><img src='' javascript='11'><img src='' onerror='alert'>";
        // 拿到匹配的pre标签List
        List<String> resultFindAll = ReUtil.findAll(regex, content, 0, new ArrayList<>());
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
            System.out.println("pre标签替换");
            System.out.println(preFilter);
            final String[] filterResult = {HtmlUtil.filter(preFilter)};
            resultFindAll.forEach(obj -> filterResult[0] = ReUtil.replaceFirst(pattern, filterResult[0], obj));
            System.out.println("pre标签被换回来了");
            System.out.println(filterResult[0]);
            assertEquals(filterResult[0], replacePreTagAndFilterXss(content));
        } else {
            String filterResult = HtmlUtil.filter(content);
            System.out.println("HtmlUtil.filter");
            System.out.println(filterResult);
        }
    }
}
