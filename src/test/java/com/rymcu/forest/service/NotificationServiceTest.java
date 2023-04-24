package com.rymcu.forest.service;

import com.rymcu.forest.base.BaseServiceTest;
import com.rymcu.forest.core.constant.NotificationConstant;
import com.rymcu.forest.dto.NotificationDTO;
import com.rymcu.forest.entity.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 消息通知接口测试
 */
@Order(1)
class NotificationServiceTest extends BaseServiceTest {
    private final Long idUser = 2L;
    private final Long dataId = 1L;
    private final String dataSummary = "nickname 关注了你!";
    @Autowired
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        save();
    }

    @Test
    @DisplayName("获取未读消息数据")
    void findUnreadNotifications() {
        List<Notification> unreadNotifications = notificationService.findUnreadNotifications(idUser);
        assertFalse(unreadNotifications.isEmpty());
    }

    @Test
    @DisplayName("获取消息数据")
    void findNotifications() {
        List<NotificationDTO> notifications = notificationService.findNotifications(idUser);
        assertNotNull(notifications);
    }

    @Test
    @DisplayName("获取消息数据")
    void findNotification() {
        Notification notification = notificationService.findNotification(idUser, dataId, NotificationConstant.Follow);
        assertNotNull(notification);
        assertEquals(idUser, notification.getIdUser());
        assertEquals(dataSummary, notification.getDataSummary());

        Notification notification2 = notificationService.findNotification(0L, dataId, NotificationConstant.Follow);
        assertNull(notification2);
    }

    @DisplayName("创建系统通知")
    void save() {
        List<Notification> notifications = notificationService.findUnreadNotifications(idUser);
        int size = notifications.size();
        Integer integer = notificationService.save(idUser, dataId, NotificationConstant.Follow, dataSummary);
        assertEquals(1, integer);
        notifications = notificationService.findUnreadNotifications(idUser);
        assertEquals(size + 1, notifications.size());
    }

    @Test
    @DisplayName("标记消息已读")
    void readNotification() {
        List<Notification> notifications = notificationService.findUnreadNotifications(idUser);
        int size = notifications.size();
//        Integer integer = notificationService.deleteUnreadNotification(idUser, NotificationConstant.Follow);
//        assertEquals(1, integer);

        Integer integer = notificationService.readNotification(notifications.get(0).getIdNotification(), idUser);
        assertNotEquals(0, integer);

        notifications = notificationService.findUnreadNotifications(idUser);
        assertEquals(size - 1, notifications.size());
    }

    /**
     * 测试太麻烦测试不了,
     * 测试还得模拟向redis存入数据不建议测试
     */
    @Test
    @DisplayName("标记所有消息已读")
    void readAllNotification() {
        assertNotEquals(0, notificationService.readAllNotification(idUser));
    }

    @Test
    @DisplayName("删除相关未读消息")
    void deleteUnreadNotification() {
        List<Notification> notifications = notificationService.findUnreadNotifications(idUser);
        int size = notifications.size();
        System.out.println(size);
        Integer integer = notificationService.deleteUnreadNotification(dataId, NotificationConstant.Follow);
        assertNotEquals(0, integer);

        integer = notificationService.deleteUnreadNotification(dataId, NotificationConstant.Follow);
        assertEquals(0, integer);

        notifications = notificationService.findUnreadNotifications(idUser);
        assertEquals(0, notifications.size());
    }
}
