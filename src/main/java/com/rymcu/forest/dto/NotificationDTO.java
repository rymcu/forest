package com.rymcu.forest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rymcu.forest.entity.Notification;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ronger
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NotificationDTO extends Notification {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long idNotification;

    private String dataTitle;

    private String dataUrl;

    private Author author;

}
