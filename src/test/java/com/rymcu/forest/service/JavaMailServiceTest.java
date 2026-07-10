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
import com.rymcu.forest.dto.NotificationDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailParseException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * javaMail测试
 */
class JavaMailServiceTest extends BaseServiceTest {

    private static final String REALITY_EMAIL = "1421374934@qq.com";
    @Autowired
    private JavaMailService javaMailService;

    @Test
    void sendEmailCode() throws MessagingException {
        assertEquals(1, javaMailService.sendEmailCode(REALITY_EMAIL));
    }

    @Test
    void sendForgetPasswordEmail() throws MessagingException {
        assertEquals(1, javaMailService.sendForgetPasswordEmail(REALITY_EMAIL));
    }

    @Test
    void sendNotification() throws MessagingException {
        assertEquals(0, javaMailService.sendNotification(new NotificationDTO()));

    }
}
