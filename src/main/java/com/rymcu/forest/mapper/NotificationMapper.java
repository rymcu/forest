package com.rymcu.forest.mapper;

import com.rymcu.forest.core.mapper.Mapper;
import com.rymcu.forest.dto.NotificationDTO;
import com.rymcu.forest.entity.Notification;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ronger
 */
public interface NotificationMapper extends Mapper<Notification> {

    /**
     * 获取未读通知数据
     * @param idUser
     * @return
     */
    List<Notification> selectUnreadNotifications(@Param("idUser") Integer idUser);

    /**
     * 获取消息数据
     * @param idUser
     * @return
     */
    List<NotificationDTO> selectNotifications(@Param("idUser") Integer idUser);

    /**
     * 获取消息数据
     * @param idUser
     * @param dataId
     * @param dataType
     * @return
     */
    Notification selectNotification(@Param("idUser") Integer idUser, @Param("dataId") Integer dataId, @Param("dataType") String dataType);

    /**
     * 创建消息通知
     * @param idUser
     * @param dataId
     * @param dataType
     * @param dataSummary
     * @return
     */
    Integer insertNotification(@Param("idUser") Integer idUser, @Param("dataId") Integer dataId, @Param("dataType") String dataType, @Param("dataSummary") String dataSummary);

    /**
     * 标记消息已读
     * @param id
     * @return
     */
    Integer readNotification(@Param("id") Integer id);
}
