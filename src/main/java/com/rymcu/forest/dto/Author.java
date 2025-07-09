package com.rymcu.forest.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ronger
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    private Long idUser;

    private String userNickname;

    private String userAccount;

    private String userAvatarURL;

    private String userArticleCount;

}
