package com.rymcu.forest.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author ronger
 */
@Data
public class TopicTagDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long idTopic;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long idTag;
}
