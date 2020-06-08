package com.rymcu.vertical.service;

import com.rymcu.vertical.core.service.Service;
import com.rymcu.vertical.entity.WxUser;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

/**
 * @author ronger
 */
public interface WxUserService extends Service<WxUser> {

    WxUser saveUser(WxMpUser wxMpUser, String appId);

}
