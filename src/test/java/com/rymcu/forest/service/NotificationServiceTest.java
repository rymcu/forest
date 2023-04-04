package com.rymcu.forest.service;

import com.rymcu.forest.base.BaseServiceTest;
import com.rymcu.forest.dto.NotificationDTO;
import com.rymcu.forest.entity.Notification;
import org.apache.shiro.authz.UnauthenticatedException;
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
    private final Long idUser = 1L;
    private final Long dataId = 1L;
    private final String datatype = "1";
    private final String dataSummary = "nickname 关注了你!";
    @Autowired
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("获取未读消息数据")
    void findUnreadNotifications() {
        List<Notification> unreadNotifications = notificationService.findUnreadNotifications(1L);
        assertFalse(unreadNotifications.isEmpty());
        assertEquals(1, unreadNotifications.size());
    }

    @Test
    @DisplayName("获取消息数据")
    void findNotifications() {
        List<NotificationDTO> notifications = notificationService.findNotifications(1L);
        assertFalse(notifications.isEmpty());
        assertEquals(1, notifications.size());
    }

    @Test
    @DisplayName("获取消息数据")
    void findNotification() {

        Notification notification = notificationService.findNotification(idUser, dataId, datatype);

        assertNotNull(notification);
        assertEquals(idUser, notification.getIdUser());
        assertEquals(dataSummary, notification.getDataSummary());

        Notification notification2 = notificationService.findNotification(0L, dataId, datatype);
        assertNull(notification2);
    }

    @Test
    @DisplayName("创建系统通知")
    void save() {
        List<Notification> notifications = notificationService.findUnreadNotifications(idUser);
        assertEquals(1, notifications.size());
        Integer integer = notificationService.save(idUser, 2L, datatype, dataSummary);
        assertEquals(1, integer);

        notifications = notificationService.findUnreadNotifications(idUser);
        assertEquals(2, notifications.size());
    }

    @Test
    @DisplayName("标记消息已读")
    void readNotification() {
        List<Notification> notifications = notificationService.findUnreadNotifications(idUser);
        assertEquals(1, notifications.size());
        Integer integer = notificationService.deleteUnreadNotification(idUser, datatype);
        assertEquals(1, integer);

        integer = notificationService.readNotification(1L, idUser);
        assertEquals(0, integer);

        notifications = notificationService.findUnreadNotifications(idUser);
        assertEquals(0, notifications.size());
    }

    /**
     * 测试太麻烦测试不了,
     * 测试还得模拟向redis存入数据不建议测试
     */
    @Test
    @DisplayName("标记所有消息已读")
    void readAllNotification() {
        assertThrows(UnauthenticatedException.class, () -> notificationService.readAllNotification());
    }

    @Test
    @DisplayName("删除相关未读消息")
    void deleteUnreadNotification() {
        List<Notification> notifications = notificationService.findUnreadNotifications(idUser);
        assertEquals(1, notifications.size());
        Integer integer = notificationService.deleteUnreadNotification(idUser, datatype);
        assertEquals(1, integer);

        integer = notificationService.deleteUnreadNotification(idUser, datatype);
        assertEquals(0, integer);

        notifications = notificationService.findUnreadNotifications(idUser);
        assertEquals(0, notifications.size());


    }
}