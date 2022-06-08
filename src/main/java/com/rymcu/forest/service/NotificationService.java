package com.rymcu.forest.service;

import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.dto.NotificationDTO;
import com.rymcu.forest.entity.Notification;
import com.rymcu.forest.web.api.exception.BaseApiException;

import java.util.List;

/**
 * 消息通知接口类
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
    List<NotificationDTO> findNotifications(Integer idUser);

    /**
     * 获取消息数据
     * @param idUser
     * @param dataId
     * @param dataType
     * @return
     */
    Notification findNotification(Integer idUser, Integer dataId, String dataType);

    /**
     * 创建系统通知
     * @param idUser
     * @param dataId
     * @param dataType
     * @param dataSummary
     * @return
     */
    Integer save(Integer idUser, Integer dataId, String dataType, String dataSummary);

    /**
     * 标记消息已读
     * @param id
     * @return
     */
    Integer readNotification(Integer id);

    /**
     * 标记所有消息已读
     * @return
     * @throws BaseApiException
     */
    Integer readAllNotification() throws BaseApiException;

    /**
     * 删除相关未读消息
     * @param dataId
     * @param dataType
     * @return
     */
    Integer deleteUnreadNotification(Integer dataId, String dataType);
}
