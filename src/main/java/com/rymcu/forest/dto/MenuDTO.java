package com.rymcu.forest.dto;

import lombok.Data;

/**
 * @author ronger
 */
@Data
public class MenuDTO {

    private String id;

    private String parentId;

    private String parentName;

    private String name;

    private Long sort;

    private String href;

    private String menuType;

    private String permission;

    private String remarks;

    private String status;
}
