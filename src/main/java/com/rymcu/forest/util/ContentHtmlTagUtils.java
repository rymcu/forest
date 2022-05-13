package com.rymcu.forest.util;

import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HtmlUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * 内容html标签
 * @author 你一个人在这儿干嘛你是来拉屎的吧
 */
public class ContentHtmlTagUtils {
    private static final String regex = "(<pre>[\\s|\\S]+?</pre>)|(<code>[\\s|\\S]+?</code>)";
    /**
     * 替换pre标签，过滤xss，并把pre标签换回来
     * @param content 待处理内容
     * @return 替换pre标签，过滤xss，并把pre标签换回来后的内容
     */
    public static String replacePreTagAndFilterXss(String content) {
        if(isBlank(content)) {
            return  content;
        }
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
            // 拦截xss
            final String[] filterResult = {HtmlUtil.filter(preFilter)};

            // 依次将替换后的pre标签换回来
            resultFindAll.forEach(obj -> filterResult[0] = ReUtil.replaceFirst(pattern, filterResult[0], obj));
            return filterResult[0];
        } else {
            return HtmlUtil.filter(content);
        }
    }
}
