package com.asell.sell.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商品状态
 */
@Getter
@AllArgsConstructor
public enum ProductStatusEnum {

    UP(0, "正常"), //正常

    DOWN(1, "下架") //下架
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
