package com.asell.sell.repository;

import com.asell.sell.entity.OrderDetail;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailRepositoryTest {

    @Autowired
    private OrderDetailRepository repository;

    @Test
    public void save(){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setDetailId("123456789");
        orderDetail.setOrderId("22333");
        orderDetail.setProductId("123456");
        orderDetail.setProductName("皮蛋粥");
        orderDetail.setProductPrice(new BigDecimal(3.2));
        orderDetail.setProductQuantity(2);
        orderDetail.setProductIcon("http://xxxxx.jpg");

        OrderDetail result = repository.save(orderDetail);

        Assert.assertNotNull(result);

    }

    @Test
    public void findByOrderId() {
        List<OrderDetail> result = repository.findByOrderId("22333");

        Assert.assertNotEquals(0, result.size());
    }
}