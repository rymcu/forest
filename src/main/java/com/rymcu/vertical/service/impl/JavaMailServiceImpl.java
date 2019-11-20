package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.service.redis.RedisService;
import com.rymcu.vertical.service.JavaMailService;
import com.rymcu.vertical.util.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Properties;

@Service
public class JavaMailServiceImpl implements JavaMailService {

    @Resource
    private JavaMailSenderImpl mailSender;
    @Resource
    private RedisService redisService;

    @Value("${spring.mail.host}")
    private String SERVER_HOST;
    @Value("${spring.mail.port}")
    private String SERVER_PORT;
    @Value("${spring.mail.username}")
    private String USERNAME;
    @Value("${spring.mail.password}")
    private String PASSWORD;

    @Override
    public Integer sendEmailCode(String email) {
        return sendCode(email,0);
    }

    private Integer sendCode(String to, Integer type) {
        Properties props = new Properties();
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", SERVER_HOST);
        props.put("mail.smtp.port", SERVER_PORT);
        // 如果使用ssl，则去掉使用25端口的配置，进行如下配置,
        props.put("mail.smtp.socketFactory.class", "com.rymcu.vertical.util.MailSSLSocketFactory");
        props.put("mail.smtp.socketFactory.port", SERVER_PORT);
        // 发件人的账号，填写控制台配置的发信地址,比如xxx@xxx.com
        props.put("mail.user", USERNAME);
        // 访问SMTP服务时需要提供的密码(在控制台选择发信地址进行设置)
        props.put("mail.password", PASSWORD);
        mailSender.setJavaMailProperties(props);
        Integer code = Utils.genCode();
        redisService.set(to,code,5*60);
        System.out.println(code);
        if(type == 0) {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(USERNAME);
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject("新用户注册邮箱验证");
            simpleMailMessage.setText("【RYMCU】您的校验码是 " + code + ",有效时间 5 分钟，请不要泄露验证码给其他人。如非本人操作,请忽略！");
            mailSender.send(simpleMailMessage);
            return 1;
        }
        return 0;
    }
}
