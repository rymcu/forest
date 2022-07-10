package com.rymcu.forest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author ronger
 */
@Data
public class Author {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long idUser;

    private String userNickname;

    private String userAccount;

    private String userAvatarURL;

    private String userArticleCount;

}
