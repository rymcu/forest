package com.rymcu.vertical.service;

import javax.mail.MessagingException;

/**
 * @author ronger
 * @since 2019/11/23
 * @version 1.0
 * **/
public interface JavaMailService {
    /**
     * 发送验证码邮件
     * @param email 收件人邮箱
     * @return 执行结果 0：失败1：成功
     * */
    Integer sendEmailCode(String email) throws MessagingException;

    /**
     * 发送找回密码邮件
     * @param email 收件人邮箱
     * @return 执行结果 0：失败1：成功
     * */
    Integer sendForgetPasswordEmail(String email) throws MessagingException;
}
