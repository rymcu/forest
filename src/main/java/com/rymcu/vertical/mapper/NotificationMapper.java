package com.rymcu.vertical.mapper;

import com.rymcu.vertical.core.mapper.Mapper;
import com.rymcu.vertical.entity.Notification;
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

    List<Notification> selectNotifications(@Param("idUser") Integer idUser);
}
