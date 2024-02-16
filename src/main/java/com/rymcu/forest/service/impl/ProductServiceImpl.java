package com.rymcu.forest.service.impl;

import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.dto.ProductDTO;
import com.rymcu.forest.entity.Product;
import com.rymcu.forest.mapper.ProductMapper;
import com.rymcu.forest.service.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created on 2022/6/21 9:26.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @packageName com.rymcu.forest.service.impl
 */
@Service
public class ProductServiceImpl extends AbstractService<Product> implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Override
    public List<ProductDTO> findProducts() {
        return productMapper.selectProducts();
    }

    @Override
    public ProductDTO findProductDTOById(Long idProduct, Integer type) {
        return productMapper.selectProductDTOById(idProduct, type);
    }
}
