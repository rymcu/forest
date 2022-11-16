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
     *
     * @param idUser
     * @return
     */
    List<Notification> selectUnreadNotifications(@Param("idUser") Long idUser);

    /**
     * 获取消息数据
     *
     * @param idUser
     * @return
     */
    List<NotificationDTO> selectNotifications(@Param("idUser") Long idUser);

    /**
     * 获取消息数据
     *
     * @param idUser
     * @param dataId
     * @param dataType
     * @return
     */
    Notification selectNotification(@Param("idUser") Long idUser, @Param("dataId") Long dataId, @Param("dataType") String dataType);

    /**
     * 创建消息通知
     *
     * @param idUser
     * @param dataId
     * @param dataType
     * @param dataSummary
     * @return
     */
    Integer insertNotification(@Param("idUser") Long idUser, @Param("dataId") Long dataId, @Param("dataType") String dataType, @Param("dataSummary") String dataSummary);

    /**
     * 标记消息已读
     *
     * @param id
     * @param idUser
     * @return
     */
    Integer readNotification(@Param("id") Long id, Long idUser);

    /**
     * 标记所有消息已读
     *
     * @param idUser
     * @return
     */
    Integer readAllNotification(@Param("idUser") Long idUser);

    /**
     * 删除相关未读消息
     *
     * @param dataId
     * @param dataType
     * @return
     */
    Integer deleteUnreadNotification(@Param("dataId") Long dataId, @Param("dataType") String dataType);
}
