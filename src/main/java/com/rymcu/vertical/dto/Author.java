package com.rymcu.vertical.dto;

import lombok.Data;

@Data
public class Author {

    private String idUser;

    private String userNickname;

    private String userAvatarURL;

    private Integer userArticleCount;

}
