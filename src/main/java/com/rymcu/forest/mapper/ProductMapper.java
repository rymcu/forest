package com.rymcu.forest.mapper;

import com.rymcu.forest.core.mapper.Mapper;
import com.rymcu.forest.entity.Product;
import org.apache.ibatis.annotations.Param;

/**
 * Created on 2022/6/13 21:53.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 */
public interface ProductMapper extends Mapper<Product> {
    /**
     * 保存产品详情
     * @param idProduct
     * @param productContent
     * @param productContentHtml
     * @return
     */
    Integer insertProductContent(@Param("idProduct") Integer idProduct, @Param("productContent") String productContent, @Param("productContentHtml") String productContentHtml);
}
