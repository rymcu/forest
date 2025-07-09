package com.rymcu.forest.dto;

import com.rymcu.forest.entity.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created on 2022/6/21 9:38.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @packageName com.rymcu.forest.dto
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ProductDTO extends Product {
    /**
     * 文章内容
     */
    private String productContent;
    /**
     * 文章内容html
     */
    private String productContentHtml;
}
