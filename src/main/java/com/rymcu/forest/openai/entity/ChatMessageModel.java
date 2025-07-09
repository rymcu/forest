package com.rymcu.forest.openai.entity;

import lombok.Data;

/**
 * Created on 2023/7/16 14:52.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @desc : com.rymcu.forest.openai.entity
 */
@Data
public class ChatMessageModel {

    Long dataId;

    String to;

    String from;

    Integer dataType;

    String content;

    String role;
}
