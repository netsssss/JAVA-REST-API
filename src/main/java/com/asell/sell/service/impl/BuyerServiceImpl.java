package com.asell.sell.service.impl;

import com.asell.sell.dto.OrderDTO;
import com.asell.sell.enums.ResultStatusEnum;
import com.asell.sell.exception.SellException;
import com.asell.sell.service.BuyerService;
import com.asell.sell.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private OrderService orderService;

    @Override
    public OrderDTO findOneByOrder(String openid, String orderId) {
        return checkOrderOwner(openid, orderId);
    }

    @Override
    public OrderDTO cancelOrder(String openid, String orderId) {

        OrderDTO orderDTO = checkOrderOwner(openid, orderId);

        if (orderDTO == null) {
            log.error("[取消订单]查不到订单, orderId={}", orderId);
            throw new SellException(ResultStatusEnum.ORDER_NOT_EXIST);
        }

        return orderService.cancel(orderDTO);

    }

    private OrderDTO checkOrderOwner(String openid, String orderId) {
        if(StringUtils.isEmpty(openid) || StringUtils.isEmpty(orderId)) {
            log.error("[订单详情]openid或orderId为空");
            throw new SellException(ResultStatusEnum.PARAMS_ERROR);
        }

        OrderDTO orderDTO = orderService.findById(orderId);

        if(orderDTO == null) {
            return null;
        }

        /**
         * 判断是否是自己的订单
         */
        if(!orderDTO.getBuyerOpenid().equalsIgnoreCase(openid)) {
            log.error("[订单详情]订单的openid不一致, orderDTO={}", orderDTO);
            throw new SellException(ResultStatusEnum.ORDER_OWNER_ERROR);
        }
        return orderDTO;
    }
}
