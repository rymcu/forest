package com.rymcu.forest.service;

import com.rymcu.forest.base.BaseServiceTest;
import com.rymcu.forest.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest extends BaseServiceTest {

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("查询产品列表")
    void findProducts() {
        List<ProductDTO> products = productService.findProducts();
        assertFalse(products.isEmpty());
    }

    @Test
    @DisplayName("获取产品详情")
    void findProductDTOById() {
        ProductDTO productDTOById = productService.findProductDTOById(1, null);
        assertNotNull(productDTOById);
        ProductDTO productDTOById2 = productService.findProductDTOById(1, 1);

        assertEquals(productDTOById.getProductTitle(), productDTOById2.getProductTitle());
        assertNotEquals(productDTOById.getProductContent(), productDTOById2.getProductContent());
    }
}