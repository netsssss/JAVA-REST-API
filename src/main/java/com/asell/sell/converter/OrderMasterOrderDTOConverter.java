package com.asell.sell.converter;

import com.asell.sell.dto.OrderDTO;
import com.asell.sell.entity.OrderMaster;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * convert: OrderMaster转OrderDTO
 * reverseConvert: OrderDTO转OrderMaster
 */
public class OrderMasterOrderDTOConverter {
    public static OrderDTO convert(OrderMaster orderMaster) {

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);

        return orderDTO;
    }

    public static List<OrderDTO> convert(List<OrderMaster> orderMasters) {
        return orderMasters.stream().map(e ->
                    convert(e)
                ).collect(Collectors.toList());
    }

    public static OrderMaster reverseConvert(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);

        return orderMaster;
    }

    public static List<OrderMaster> reverseConvert(List<OrderDTO> orderDTOS) {
        return orderDTOS.stream().map(e ->
                reverseConvert(e)
        ).collect(Collectors.toList());
    }
}
