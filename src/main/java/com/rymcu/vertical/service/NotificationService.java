package com.rymcu.vertical.service;

import com.rymcu.vertical.core.service.Service;
import com.rymcu.vertical.entity.Notification;
import org.apache.ibatis.annotations.Param;

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
    List<Notification> findNotifications(Integer idUser);

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
     */
    void readNotification(Integer id);
}
