package com.rymcu.forest.mapper;

import com.rymcu.forest.core.mapper.Mapper;
import com.rymcu.forest.dto.ProductDTO;
import com.rymcu.forest.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created on 2022/6/13 21:53.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 */
public interface ProductMapper extends Mapper<Product> {
    /**
     * 保存产品详情
     *
     * @param idProduct 产品主键
     * @param productContent 产品详情 markdown
     * @param productContentHtml 产品详情 html
     * @return 更新数量
     */
    Integer insertProductContent(@Param("idProduct") Long idProduct, @Param("productContent") String productContent, @Param("productContentHtml") String productContentHtml);

    /**
     * 查询产品列表
     *
     * @return 产品列表
     */
    List<ProductDTO> selectProducts();

    /**
     * 获取产品详情
     *
     * @param idProduct 产品 ID
     * @param type 获取类型
     * @return 产品信息
     */
    ProductDTO selectProductDTOById(@Param("idProduct") Long idProduct, @Param("type") Integer type);

    /**
     * 获取在线产品
     * @return 产品信息
     */
    List<ProductDTO> selectOnlineProducts();

    /**
     * 保存产品详情
     *
     * @param idProduct 产品主键
     * @param productContent 产品详情 markdown
     * @param productContentHtml 产品详情 html
     * @return 更新数量
     */
    Integer updateProductContent(@Param("idProduct") Long idProduct, @Param("productContent") String productContent, @Param("productContentHtml") String productContentHtml);

    /**
     * @param idProduct 产品主键
     * @param status 状态
     * @return 更新成功状态
     */
    int updateStatus(@Param("idProduct") Long idProduct, @Param("status") Integer status);
}
