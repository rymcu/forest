package com.rymcu.forest.openai.entity;

import lombok.Data;

import java.util.List;

/**
 * Created on 2024/8/6 10:24.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @desc : com.rymcu.forest.openai.entity
 */
@Data
public class ChatModel {

    String model;

    List<ChatMessageModel> messages;

}
