package com.rymcu.forest.wx.mp.service;

import me.chanjar.weixin.common.bean.menu.WxMenu;

import java.io.IOException;

/**
 * @author ronger
 */
public interface WxMenuService {
    /**
     * 获取公众号菜单配置
     * @return
     * @throws IOException
     */
    WxMenu getMenus() throws IOException;
}
