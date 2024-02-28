package com.rymcu.forest.service;

import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.dto.ProductDTO;
import com.rymcu.forest.entity.Product;

import java.util.List;

/**
 * Created on 2022/6/21 9:25.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @packageName com.rymcu.forest.service
 */
public interface ProductService extends Service<Product> {
    /**
     * 查询产品列表
     *
     * @return
     */
    List<ProductDTO> findProducts();

    /**
     * 获取产品详情
     *
     * @param idProduct
     * @param type
     * @return
     */
    ProductDTO findProductDTOById(Long idProduct, Integer type);

    /**
     * 获取在线商品
     * @return
     */
    List<ProductDTO> findOnlineProducts();
}
