package com.rymcu.forest.dto;


import lombok.Data;

/**
 * @author ronger
 */
@Data
public class UpdatePasswordDTO {

    private Long idUser;

    private String password;

}
