package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.service.redis.RedisService;
import com.rymcu.vertical.service.JavaMailService;
import com.rymcu.vertical.util.Utils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class JavaMailServiceImpl implements JavaMailService {

    @Resource
    private JavaMailSenderImpl mailSender;
    @Resource
    private RedisService redisService;

    @Override
    public Integer sendEmailCode(String email) {
        return sendCode(email,0);
    }

    private Integer sendCode(String to, Integer type) {
        Integer code = Utils.genCode();
        redisService.set(to,code,5*60);
        if(type == 0) {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("service@rymcu.com");
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject("新用户注册邮箱验证");
            simpleMailMessage.setText("【RYMCU】您的校验码是 " + code + ",有效时间 5 分钟，请不要泄露验证码给其他人。如非本人操作,请忽略！");
            mailSender.send(simpleMailMessage);
            return 1;
        }
        return 0;
    }
}
