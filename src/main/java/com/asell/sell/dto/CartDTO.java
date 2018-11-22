package com.asell.sell.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 购物车
 */
@Data
@AllArgsConstructor
public class CartDTO {

    /**
     * 商品Id
     */
    private String productId;

    /**
     * 数量
     */
    private Integer productQuantity;
}
