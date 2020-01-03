package com.rymcu.vertical.web.api.notification;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.vertical.core.result.GlobalResult;
import com.rymcu.vertical.core.result.GlobalResultGenerator;
import com.rymcu.vertical.entity.Notification;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.service.NotificationService;
import com.rymcu.vertical.util.UserUtils;
import com.rymcu.vertical.util.Utils;
import com.rymcu.vertical.web.api.exception.BaseApiException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 消息通知
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {

    @Resource
    private NotificationService notificationService;

    @GetMapping("/all")
    public GlobalResult notifications(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) throws BaseApiException {
        User user = UserUtils.getWxCurrentUser();
        PageHelper.startPage(page, rows);
        List<Notification> list = notificationService.findNotifications(user.getIdUser());
        PageInfo<Notification> pageInfo = new PageInfo(list);
        Map map = Utils.getNotificationsGlobalResult(pageInfo);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/unread")
    public GlobalResult unreadNotification(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) throws BaseApiException {
        User user = UserUtils.getWxCurrentUser();
        PageHelper.startPage(page, rows);
        List<Notification> list = notificationService.findUnreadNotifications(user.getIdUser());
        PageInfo<Notification> pageInfo = new PageInfo(list);
        Map map = Utils.getNotificationsGlobalResult(pageInfo);
        return GlobalResultGenerator.genSuccessResult(map);
    }

}
