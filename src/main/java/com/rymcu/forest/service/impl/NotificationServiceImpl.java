/*
 * Copyright (c) 2020 RYMCU (https://rymcu.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


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
