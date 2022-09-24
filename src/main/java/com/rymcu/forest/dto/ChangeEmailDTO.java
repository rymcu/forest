package com.rymcu.forest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author ronger
 */
@Data
public class ChangeEmailDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long idUser;

    private String email;

    private String code;

}
