package com.rymcu.vertical.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ronger
 */
@Data
public class LabelModel implements Serializable {

    private String label;

    private String value;

}
