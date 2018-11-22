package com.asell.sell.service;

import com.asell.sell.dto.CartDTO;
import com.asell.sell.entity.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 商品
 */
public interface ProductInfoService {

    /**
     * 根据ID查询一个商品
     * @param productId
     * @return
     */
    ProductInfo findById(String productId);

    /**
     * 查询所有在架商品
     * @return
     */
    List<ProductInfo> findUpAll();

    /**
     * 查询所有商品
     * @return
     */
    Page<ProductInfo> findAll(Pageable pageable);

    /**
     * 添加和修改商品
     * @param productInfo
     * @return
     */
    ProductInfo save(ProductInfo productInfo);

    /**
     * 加库存
     * @param cartDTOS
     */
    void increaseStock(List<CartDTO> cartDTOS);

    /**
     * 减库存
     * @param cartDTOS
     */
    void decreaseStock(List<CartDTO> cartDTOS);

}
