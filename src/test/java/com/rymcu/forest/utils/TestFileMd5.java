package com.rymcu.forest.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.util.DigestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

// 仅运行指定类
@SpringBootTest(classes = TestFileMd5.class)
public class TestFileMd5 {


    /**
     * 要使用spring注解，则这个类必须要交给spring管理
     */
    @Value("classpath:1.txt")
    private Resource testFile;

    /**
     * c6c26c7e8a5eb493b14e84bd91df60e3
     * d41d8cd98f00b204e9800998ecf8427e
     *
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        String md5 = DigestUtils.md5DigestAsHex(testFile.getInputStream());
        assertEquals("202cb962ac59075b964b07152d234b70", md5);
    }
}
