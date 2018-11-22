package com.asell.sell.service.impl;

import com.asell.sell.dto.OrderDTO;
import com.asell.sell.entity.OrderDetail;
import com.asell.sell.enums.OrderStatusEnum;
import com.asell.sell.enums.PayStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    private final String OPENID = "233333";

    private final String ORDER_ID = "1541654268206234837";

    @Test
    public void create() {

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setBuyerAddress("B站");
        orderDTO.setBuyerName("妮可");
        orderDTO.setBuyerPhone("00123456789");
        orderDTO.setBuyerOpenid(OPENID);

        //购物车
        List<OrderDetail> orderDetails = new ArrayList<>();

        OrderDetail o1 = new OrderDetail();
        o1.setDetailId("123456");
        o1.setProductQuantity(2);

        OrderDetail o2 = new OrderDetail();
        o2.setDetailId("654321");
        o2.setProductQuantity(1);

        orderDetails.add(o1);
        orderDetails.add(o2);

        orderDTO.setOrderDetails(orderDetails);

        OrderDTO result = orderService.create(orderDTO);

        //log.info("创建订单 --- result={}", result);

        Assert.assertNotNull(result);

    }

    @Test
    public void findById() {
        OrderDTO result = orderService.findById(ORDER_ID);
        log.info("单个={}", result);
        Assert.assertEquals(ORDER_ID, result.getOrderId());
    }

    @Test
    public void findList() {

        Pageable pageable = PageRequest.of(0, 2);

        Page<OrderDTO> orderDTOPage = orderService.findList(OPENID, pageable);

        log.info("pageis = {}", orderDTOPage.getTotalElements());

        Assert.assertNotNull(orderDTOPage);
    }

    @Test
    public void cancel() {
        OrderDTO orderDTO = orderService.findById(ORDER_ID);
        OrderDTO result = orderService.cancel(orderDTO);

        Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(), result.getOrderStatus());
    }

    @Test
    public void finish() {
        OrderDTO orderDTO = orderService.findById(ORDER_ID);
        OrderDTO result = orderService.finish(orderDTO);

        Assert.assertEquals(OrderStatusEnum.FINISHED.getCode(), result.getOrderStatus());
    }

    @Test
    public void paid() {
        OrderDTO orderDTO = orderService.findById(ORDER_ID);
        OrderDTO result = orderService.paid(orderDTO);

        Assert.assertEquals(PayStatusEnum.SUCCESS.getCode(), result.getPayStatus());
    }
}