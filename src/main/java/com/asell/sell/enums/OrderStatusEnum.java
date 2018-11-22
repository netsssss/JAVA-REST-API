package com.asell.sell.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    NEW(0, "新订单"), //新订单

    FINISHED(1, "完结"), //订单完结

    CANCEL(2, "已取消") //订单已取消
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
