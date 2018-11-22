package com.asell.sell.viewobject;

import lombok.Data;

/**
 * 返回内容最外层格式
 * @param <T>
 */
@Data
public class ResultViewObject<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;
}
