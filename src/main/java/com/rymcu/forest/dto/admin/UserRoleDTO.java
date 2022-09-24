package com.rymcu.forest.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author ronger
 */
@Data
public class UserRoleDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long idUser;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long idRole;
}
