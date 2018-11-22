package com.asell.sell.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回状态
 */
@Getter
@AllArgsConstructor
public enum ResultStatusEnum {

    SUCCESS(0, "成功"), //成功

    PARAMS_ERROR(1, "参数不正确"), //参数不正确

    PRODUCT_NOT_EXIST(10, "商品不存在"), //商品不存在

    PRODUCT_STOCK_ERROR(11, "商品库存不正确"), //商品库存不正确

    ORDER_NOT_EXIST(20, "订单不存在"), //订单不存在

    ORDER_DETAIL_NOT_EXIST(21, "订单详情不存在"), //订单详情不存在

    ORDER_STATUS_ERROR(22, "订单状态不正确"), //订单状态不正确

    ORDER_UPDATE_FAIL(23, "订单更新失败"), //订单更新失败

    ORDER_DETAIL_EMPTY(25, "订单详情为空"), //订单详情为空

    ORDER_OWNER_ERROR(26, "该订单不属于当前用户"), //该订单不属于当前用户

    ORDER_PAY_STATUS_ERROR(30, "支付状态不正确"), //支付状态不正确

    CART_EMPTY(50, "购物车为空"), //购物车为空
    ;
    /**
     * 状态码
     */
    private Integer code;

    /**
     * 信息
     */
    private String message;
}
