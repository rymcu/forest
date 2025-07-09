package com.rymcu.forest.service.impl;

import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.dto.NotificationDTO;
import com.rymcu.forest.entity.Notification;
import com.rymcu.forest.mapper.NotificationMapper;
import com.rymcu.forest.service.NotificationService;
import com.rymcu.forest.util.BeanCopierUtil;
import com.rymcu.forest.util.NotificationUtils;
import com.rymcu.forest.util.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author ronger
 */
@Service
public class NotificationServiceImpl extends AbstractService<Notification> implements NotificationService {

    @Resource
    private NotificationMapper notificationMapper;

    private final static String UN_READ = "0";

    @Override
    public List<Notification> findUnreadNotifications(Long idUser) {
        return notificationMapper.selectUnreadNotifications(idUser);
    }

    @Override
    public List<NotificationDTO> findNotifications(Long idUser) {
        List<NotificationDTO> list = notificationMapper.selectNotifications(idUser);
        list.forEach(notification -> {
            NotificationDTO notificationDTO = NotificationUtils.genNotification(notification);
            // 判断关联数据是否已删除
            if (Objects.nonNull(notificationDTO.getAuthor())) {
                BeanCopierUtil.copy(notificationDTO, notification);
            } else {
                // 关联数据已删除,且未读
                if (UN_READ.equals(notification.getHasRead())) {
                    notificationMapper.readNotification(notification.getIdNotification(), idUser);
                }
                NotificationDTO dto = new NotificationDTO();
                dto.setDataSummary("该消息已被撤销!");
                dto.setDataType("-1");
                dto.setHasRead("1");
                dto.setCreatedTime(notification.getCreatedTime());
                BeanCopierUtil.copy(dto, notification);
            }
        });
        return list;
    }

    @Override
    public Notification findNotification(Long idUser, Long dataId, String dataType) {
        return notificationMapper.selectNotification(idUser, dataId, dataType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer save(Long idUser, Long dataId, String dataType, String dataSummary) {
        return notificationMapper.insertNotification(idUser, dataId, dataType, dataSummary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer readNotification(Long id, Long idUser) {
        return notificationMapper.readNotification(id, idUser);
    }

    @Override
    public Integer readAllNotification(Long idUser) {
        return notificationMapper.readAllNotification(idUser);
    }

    @Override
    public Integer deleteUnreadNotification(Long dataId, String dataType) {
        return notificationMapper.deleteUnreadNotification(dataId, dataType);
    }
}
