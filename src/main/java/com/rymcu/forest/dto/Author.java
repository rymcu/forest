package com.rymcu.forest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long idUser;

    private String userNickname;

    private String userAccount;

    private String userAvatarURL;

    private String userArticleCount;

}
