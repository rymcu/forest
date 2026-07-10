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


package com.rymcu.forest.service.impl;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.entity.LoginRecord;
import com.rymcu.forest.mapper.LoginRecordMapper;
import com.rymcu.forest.service.LoginRecordService;
import com.rymcu.forest.util.Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created on 2022/1/14 8:48.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @packageName com.rymcu.forest.service.impl
 */
@Service
public class LoginRecordServiceImpl extends AbstractService<LoginRecord> implements LoginRecordService {

    @Resource
    private LoginRecordMapper loginRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginRecord saveLoginRecord(Long idUser) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = Utils.getIpAddress(request);
        String ua = request.getHeader("user-agent");
        String fingerprint = request.getHeader("fingerprint");
        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setIdUser(idUser);
        loginRecord.setLoginIp(ip);
        loginRecord.setLoginUa(ua);
        loginRecord.setLoginDeviceId(fingerprint);
        UserAgent userAgent = UserAgentUtil.parse(ua);
        loginRecord.setLoginOS(userAgent.getOs().toString());
        loginRecord.setLoginBrowser(userAgent.getBrowser().toString());
        loginRecord.setCreatedTime(new Date());
        loginRecordMapper.insertSelective(loginRecord);
        return loginRecord;
    }

    @Override
    public List<LoginRecord> findLoginRecordByIdUser(Integer idUser) {
        return loginRecordMapper.selectLoginRecordByIdUser(idUser);
    }
}
