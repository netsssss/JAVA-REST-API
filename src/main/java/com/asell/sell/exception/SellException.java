package com.asell.sell.exception;

import com.asell.sell.enums.ResultStatusEnum;

/**
 * 订单异常
 */
public class SellException extends RuntimeException {
    private static final long serialVersionUID = -5171463557264434123L;
    private Integer code;

    public SellException(ResultStatusEnum resultStatusEnum) {
        super(resultStatusEnum.getMessage());

        this.setCode(resultStatusEnum.getCode());
    }

    /**
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    public SellException(Integer code, String message) {
        super(message);

        this.setCode(code);
    }
}
