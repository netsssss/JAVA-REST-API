package com.asell.sell.converter;

import com.asell.sell.dto.OrderDTO;
import com.asell.sell.entity.OrderDetail;
import com.asell.sell.enums.ResultStatusEnum;
import com.asell.sell.exception.SellException;
import com.asell.sell.form.OrderForm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * OrderForm转OrderDTO
 */
@Slf4j
public class OrderFormOrderDTOConverter {

    public static OrderDTO convert(OrderForm orderForm) {

        Gson gson = new Gson();

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());

        List<OrderDetail> orderDetails = new ArrayList<>();

        /**
         * 判断前端返回值是否为json格式
         */
        try {
            orderDetails = gson.fromJson(orderForm.getItems(), new TypeToken<List<OrderDetail>>() {
            }.getType());
        }catch (Exception e){
            log.error("[对象转换]错误, string={}", orderForm.getItems());
            throw new SellException(ResultStatusEnum.PARAMS_ERROR);
        }

        orderDTO.setOrderDetails(orderDetails);

        return orderDTO;
    }
}
