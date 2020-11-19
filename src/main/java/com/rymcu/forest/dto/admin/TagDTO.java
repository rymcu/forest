package com.rymcu.forest.dto.admin;

import com.rymcu.forest.dto.Author;
import lombok.Data;

/**
 * @author ronger
 */
@Data
public class TagDTO {

    private Integer idTag;

    private String tagTitle;

    private String tagUri;

    private String tagDescription;

    private String tagIconPath;

    private Integer tagAuthorId;

    private Author tagAuthor;

}
