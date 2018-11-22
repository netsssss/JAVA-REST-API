package com.asell.sell.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付状态
 */
@Getter
@AllArgsConstructor
public enum PayStatusEnum {

    WAIT(0, "未支付"), //未支付

    SUCCESS(1, "支付成功"), //支付成功
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
