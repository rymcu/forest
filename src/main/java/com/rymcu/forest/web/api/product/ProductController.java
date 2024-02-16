package com.rymcu.forest.web.api.product;

import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.dto.ProductDTO;
import com.rymcu.forest.service.ProductService;
import org.springframework.web.bind.annotation.*;

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



    @GetMapping("/detail/{idProduct}")
    public GlobalResult<ProductDTO> detail(@PathVariable Long idProduct, @RequestParam(defaultValue = "2") Integer type) {
        ProductDTO dto = productService.findProductDTOById(idProduct, type);
        return GlobalResultGenerator.genSuccessResult(dto);
    }

}
