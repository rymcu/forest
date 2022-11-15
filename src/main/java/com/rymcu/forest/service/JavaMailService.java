package com.rymcu.forest.service;

import com.rymcu.forest.dto.NotificationDTO;

import javax.mail.MessagingException;

/**
 * @author ronger
 * @version 1.0
 * @since 2019/11/23
 **/
public interface JavaMailService {
    /**
     * 发送验证码邮件
     *
     * @param email 收件人邮箱
     * @return 执行结果 0：失败1：成功
     * @throws MessagingException
     */
    Integer sendEmailCode(String email) throws MessagingException;

    /**
     * 发送找回密码邮件
     *
     * @param email 收件人邮箱
     * @return 执行结果 0：失败1：成功
     * @throws MessagingException
     */
    Integer sendForgetPasswordEmail(String email) throws MessagingException;

    /**
     * 发送下消息通知
     *
     * @param notification
     * @return
     * @throws MessagingException
     */
    Integer sendNotification(NotificationDTO notification) throws MessagingException;
}
