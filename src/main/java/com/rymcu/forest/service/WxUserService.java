package com.rymcu.forest.service;

import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.entity.WxUser;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

/**
 * @author ronger
 */
public interface WxUserService extends Service<WxUser> {

    WxUser saveUser(WxMpUser wxMpUser, String appId);

}
