package com.rymcu.forest.service;

import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.entity.LoginRecord;

/**
 * Created on 2022/1/14 8:47.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @packageName com.rymcu.forest.service
 */
public interface LoginRecordService extends Service<LoginRecord> {
    /**
     * 保存登录记录
     * @param idUser
     * @return
     */
    LoginRecord saveLoginRecord(Integer idUser);
}
