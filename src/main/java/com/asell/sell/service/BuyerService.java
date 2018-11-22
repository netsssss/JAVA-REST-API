package com.asell.sell.service;

import com.asell.sell.dto.OrderDTO;

/**
 * 买家
 */
public interface BuyerService {

    /**
     * 查询一个订单
     */
    OrderDTO findOneByOrder(String openid, String orderId);
    /**
     * 取消订单
     */
    OrderDTO cancelOrder(String openid, String orderId);
}
