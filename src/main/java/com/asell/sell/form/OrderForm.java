package com.asell.sell.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 订单表单
 */
@Data
public class OrderForm {
    /**
     * 名字
     */
    @NotEmpty(message = "名字必填")
    private String name;

    /**
     * 电话
     */
    @NotEmpty(message = "电话必填")
    private String phone;

    /**
     * 地址
     */
    @NotEmpty(message = "地址必填")
    private String address;

    /**
     * openid
     */
    @NotEmpty(message = "openid必填")
    private String openid;

    /**
     * 购物车
     */
    @NotEmpty(message = "购物车不能为空")
    private String items;
}
