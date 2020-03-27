package com.rymcu.vertical.dto;

import lombok.Data;

/**
 * @author ronger
 */
@Data
public class UserDTO {

    private Integer idUser;

    private String account;

    private String avatarType;

    private String avatarUrl;

    private String nickname;
}
