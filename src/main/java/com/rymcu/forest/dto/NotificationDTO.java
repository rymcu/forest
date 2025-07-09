package com.rymcu.forest.dto;


import com.rymcu.forest.entity.Notification;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ronger
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NotificationDTO extends Notification {

    private Long idNotification;

    private String dataTitle;

    private String dataUrl;

    private Author author;

}
