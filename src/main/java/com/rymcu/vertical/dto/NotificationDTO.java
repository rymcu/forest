package com.rymcu.vertical.dto;

import com.rymcu.vertical.entity.Notification;
import lombok.Data;

/**
 * @author ronger
 */
@Data
public class NotificationDTO extends Notification {

    private Integer idNotification;

    private String dataTitle;

    private String dataUrl;

    private Author author;

}
