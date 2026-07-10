/*
 * Copyright (c) 2020 RYMCU (https://rymcu.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package com.rymcu.forest.service;

import com.rymcu.forest.base.BaseServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ForestFileServiceTest extends BaseServiceTest {

    private final String fileUrl = "localhost/upload/file/123.jpg";
    private final String filePath = "upload/file/123.jpg";
    private final String md5Value = "md5Value";
    private final long createdBy = 1L;
    private final long fileSize = 1024L;
    private final String fileType = "jpg";

    @Autowired
    private ForestFileService forestFileService;

    @BeforeEach
    void setUp() {
        forestFileService.insertForestFile(fileUrl, filePath, md5Value, createdBy, fileSize, fileType);
    }

    @Test
    @DisplayName("通过md5获取文件访问链接")
    void getFileUrlByMd5() {
        String fileUrlByMd5 = forestFileService.getFileUrlByMd5(md5Value, createdBy, fileType);
        assertEquals(fileUrl, fileUrlByMd5);
    }

    @Test
    @DisplayName("插入文件对象")
    void insertForestFile() {
        int i = forestFileService.insertForestFile(fileUrl, filePath, md5Value, createdBy, fileSize, fileType);
        assertEquals(1, i);
    }
}