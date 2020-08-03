package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.entity.Notification;
import com.rymcu.vertical.mapper.NotificationMapper;
import com.rymcu.vertical.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ronger
 */
@Service
public class NotificationServiceImpl extends AbstractService<Notification> implements NotificationService {

    @Resource
    private NotificationMapper notificationMapper;

    @Override
    public List<Notification> findUnreadNotifications(Integer idUser){
        List<Notification> list = notificationMapper.selectUnreadNotifications(idUser);
        return list;
    }

    @Override
    public List<Notification> findNotifications(Integer idUser) {
        List<Notification> list = notificationMapper.selectNotifications(idUser);
        return list;
    }

    @Override
    public Notification findNotification(Integer idUser, Integer dataId, String dataType) {
        return notificationMapper.selectNotification(idUser,dataId,dataType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer save(Integer idUser, Integer dataId, String dataType, String dataSummary) {
        return notificationMapper.insertNotification(idUser,dataId,dataType,dataSummary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer readNotification(Integer id) {
        return notificationMapper.readNotification(id);
    }
}
