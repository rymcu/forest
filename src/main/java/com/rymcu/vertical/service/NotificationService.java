package com.rymcu.vertical.service;

import com.rymcu.vertical.core.service.Service;
import com.rymcu.vertical.entity.Notification;

import java.util.List;

/**
 * @author ronger
 */
public interface NotificationService extends Service<Notification> {
    /**
     * 获取未读消息数据
     * @param idUser
     * @return
     */
    List<Notification> findUnreadNotifications(Integer idUser);

    /**
     * 获取消息数据
     * @param idUser
     * @return
     */
    List<Notification> findNotifications(Integer idUser);
}
