package com.rymcu.vertical.dto;

import com.rymcu.vertical.entity.Notification;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ronger
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NotificationDTO extends Notification {

    private Integer idNotification;

    private String dataTitle;

    private String dataUrl;

    private Author author;

}
