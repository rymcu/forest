package com.rymcu.forest.dto;

import lombok.Data;

@Data
public class ForgetPasswordDTO {
    private String code;

    private String password;
}
