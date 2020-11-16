package com.rymcu.vertical.dto;

import lombok.Data;

/**
 * @author ronger
 */
@Data
public class ChangeEmailDTO {

    private Integer idUser;

    private String email;

    private String code;

}
