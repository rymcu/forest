package com.rymcu.forest.dto.baidu;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ronger
 */
@Data
public class TagNlpDTO {

    private BigDecimal score;

    private String tag;

}
