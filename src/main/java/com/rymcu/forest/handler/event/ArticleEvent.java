package com.rymcu.forest.handler.event;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created on 2022/8/16 20:56.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 */
@Data
@AllArgsConstructor
public class ArticleEvent {

    private Long idArticle;

    private String articleTitle;

    private Boolean isUpdate;

    private Boolean notification;

    private String nickname;

    private Long articleAuthorId;
}
