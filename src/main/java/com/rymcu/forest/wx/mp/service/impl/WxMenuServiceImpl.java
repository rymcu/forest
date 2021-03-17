package com.rymcu.forest.wx.mp.service.impl;

import com.rymcu.forest.wx.mp.service.WxMenuService;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author ronger
 */
@Service
public class WxMenuServiceImpl implements WxMenuService {
    @Value("classpath:wxMpMenus.json")
    private Resource menuResource;

    @Override
    public WxMenu getMenus() throws IOException {
        File file = menuResource.getFile();
        String menuJson = this.jsonRead(file);
        WxMenu wxMenu = WxMenu.fromJson(menuJson);
        return wxMenu;
    }

    private String jsonRead(File file) {
        Scanner scanner = null;
        StringBuilder buffer = new StringBuilder();
        try {
            scanner = new Scanner(file, "utf-8");
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine());
            }
        } catch (Exception e) {

        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return buffer.toString();
    }
}
