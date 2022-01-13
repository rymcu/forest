package com.rymcu.forest.utils;

import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class TestFileMd5 {

    /**
     * c6c26c7e8a5eb493b14e84bd91df60e3
     * d41d8cd98f00b204e9800998ecf8427e
     *
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        String pathName = "E:\\1.txt";
        InputStream inputStream = new FileInputStream(new File(pathName));
        String md5 = DigestUtils.md5DigestAsHex((inputStream));
        System.err.println(md5);
        System.err.println(md5.length());
    }
}
