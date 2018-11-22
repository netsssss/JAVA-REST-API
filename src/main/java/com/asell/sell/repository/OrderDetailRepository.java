package com.asell.sell.repository;

import com.asell.sell.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 订单详情DAO
 */
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {

    /**
     * 根据订单Id查询
     * @param orderId
     * @return
     */
    List<OrderDetail> findByOrderId(String orderId);
}