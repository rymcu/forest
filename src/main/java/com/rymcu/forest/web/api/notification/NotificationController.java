package com.rymcu.forest.web.api.notification;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.dto.NotificationDTO;
import com.rymcu.forest.entity.Notification;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.service.NotificationService;
import com.rymcu.forest.util.UserUtils;
import com.rymcu.forest.util.Utils;
import com.rymcu.forest.web.api.exception.BaseApiException;
import com.rymcu.forest.web.api.exception.ErrorCode;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        User user = UserUtils.getCurrentUserByToken();
        if (Objects.isNull(user)) {
            throw new BaseApiException(ErrorCode.TOKEN_);
        }
        PageHelper.startPage(page, rows);
        List<NotificationDTO> list = notificationService.findNotifications(user.getIdUser());
        PageInfo<NotificationDTO> pageInfo = new PageInfo(list);
        Map map = Utils.getNotificationDTOsGlobalResult(pageInfo);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/unread")
    public GlobalResult unreadNotification(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) throws BaseApiException {
        User user = UserUtils.getCurrentUserByToken();
        if (Objects.isNull(user)) {
            throw new BaseApiException(ErrorCode.TOKEN_);
        }
        PageHelper.startPage(page, rows);
        List<Notification> list = notificationService.findUnreadNotifications(user.getIdUser());
        PageInfo<Notification> pageInfo = new PageInfo(list);
        Map map = Utils.getNotificationsGlobalResult(pageInfo);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PutMapping("/read/{id}")
    public GlobalResult read(@PathVariable Integer id) {
        Integer result = notificationService.readNotification(id);
        if (result == 0) {
            return GlobalResultGenerator.genErrorResult("标记已读失败");
        }
        return GlobalResultGenerator.genSuccessResult("标记已读成功");
    }

    @PutMapping("/read-all")
    public GlobalResult readAll() throws BaseApiException {
        Integer result = notificationService.readAllNotification();
        if (result == 0) {
            return GlobalResultGenerator.genErrorResult("标记已读失败");
        }
        return GlobalResultGenerator.genSuccessResult("标记已读成功");
    }

}
