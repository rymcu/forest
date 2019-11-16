package com.rymcu.vertical.dto;

import lombok.Data;

@Data
public class MenuDTO {

    private String id;

    private String parentId;

    private String parentName;

    private String name;

    private Long sort = 50L;

    private String href;

    private String menuType = "0";

    private String permission;

    private String remarks;

    private String status = "0";
}
