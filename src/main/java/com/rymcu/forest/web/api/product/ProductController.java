package com.rymcu.forest.web.api.product;

import com.rymcu.forest.service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created on 2022/6/21 9:30.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @packageName com.rymcu.forest.web.api.product
 */
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Resource
    private ProductService productService;

}
