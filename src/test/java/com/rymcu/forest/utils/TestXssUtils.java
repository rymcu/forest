package com.rymcu.forest.utils;

import com.rymcu.forest.util.XssUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


@SpringBootTest(classes = TestXssUtils.class)
public class TestXssUtils {

    private String content = null;
    /**
     * bug文章
     */
    @Value("classpath:article/135.txt")
    private Resource testFile;

    /**
     * 初始化文章内容
     */
    @BeforeEach
    public void init() {
        StringBuilder sb = new StringBuilder();
        try (FileReader fileReader = new FileReader(testFile.getFile());
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s);
            }
            content = sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Illegal group reference:参数replacement中出现符号“$”
     */
    @Test
    public void filterHtmlCode() {
        String aritcle = content;
        String s = XssUtils.filterHtmlCode(aritcle);
        System.err.println(s);
    }
}
