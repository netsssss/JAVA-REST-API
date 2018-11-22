package com.asell.sell.service.impl;

import com.asell.sell.Utils.KeyUtil;
import com.asell.sell.converter.OrderMasterOrderDTOConverter;
import com.asell.sell.dto.CartDTO;
import com.asell.sell.dto.OrderDTO;
import com.asell.sell.entity.OrderDetail;
import com.asell.sell.entity.OrderMaster;
import com.asell.sell.entity.ProductInfo;
import com.asell.sell.enums.OrderStatusEnum;
import com.asell.sell.enums.PayStatusEnum;
import com.asell.sell.enums.ResultStatusEnum;
import com.asell.sell.exception.SellException;
import com.asell.sell.repository.OrderDetailRepository;
import com.asell.sell.repository.OrderMasterRepository;
import com.asell.sell.service.OrderService;
import com.asell.sell.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {

        /**
         * 生成订单Id
         */
        String orderId = KeyUtil.genUniqueKey();
        /**
         * 初始化订单总价
         */
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);

        /**
         * 查询商品数量、价格
         */
        for(OrderDetail orderDetail : orderDTO.getOrderDetails()){

            ProductInfo productInfo;

            try {
                productInfo = productInfoService.findById(orderDetail.getProductId());
            }catch (Exception e) {
                throw new SellException(ResultStatusEnum.PRODUCT_NOT_EXIST);
            }

            /**
             * 计算订单总价
             */
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity())) //一件商品的总价
                    .add(orderAmount);

            /**
             * 订单详情入库
             */
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            orderDetailRepository.save(orderDetail);
        }

        /**
         * 订单数据入库
         */
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        orderDTO.setOrderAmount(orderAmount);
        orderDTO.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderDTO.setPayStatus(PayStatusEnum.WAIT.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);

        orderMasterRepository.save(orderMaster);

        /**
         * 扣库存
         */
        List<CartDTO> cartDTOS = orderDTO.getOrderDetails().stream().map(e ->
                new CartDTO(e.getProductId(), e.getProductQuantity())
                ).collect(Collectors.toList());

        productInfoService.decreaseStock(cartDTOS);

        return orderDTO;
    }

    @Override
    public OrderDTO findById(String orderId) {

        OrderMaster orderMaster = orderMasterRepository.findById(orderId).get();

        if(orderMaster == null) {
            throw new SellException(ResultStatusEnum.ORDER_NOT_EXIST);
        }

        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        if(CollectionUtils.isEmpty(orderDetails)) {
            throw new SellException(ResultStatusEnum.ORDER_DETAIL_NOT_EXIST);
        }

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetails(orderDetails);

        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {

        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);

        List<OrderDTO> orderDTOS = OrderMasterOrderDTOConverter.convert(orderMasterPage.getContent());

        Page<OrderDTO> orderDTOPage = new PageImpl<>(orderDTOS, pageable, orderMasterPage.getTotalElements());

        return orderDTOPage;
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        /**
         * 判断订单状态
         */
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("[取消订单]订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultStatusEnum.ORDER_STATUS_ERROR);
        }

        /**
         * 修改订单状态
         */
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        OrderMaster orderMaster = OrderMasterOrderDTOConverter.reverseConvert(orderDTO);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if(updateResult == null) {
            log.error("[取消订单]更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultStatusEnum.ORDER_UPDATE_FAIL);
        }

        /**
         * 返还库存
         */
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetails())) {
            log.error("[取消订单]订单中无商品详情, orderDTO={}", orderDTO);
            throw new SellException(ResultStatusEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOS = orderDTO.getOrderDetails().stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productInfoService.increaseStock(cartDTOS);

        /**
         * 如果已支付, 需要退款
         */
        if(orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            //TODO
        }

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        /**
         * 判断订单状态
         */
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("[完结订单]订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultStatusEnum.ORDER_STATUS_ERROR);
        }

        /**
         * 修改订单状态
         */
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = OrderMasterOrderDTOConverter.reverseConvert(orderDTO);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if(updateResult == null) {
            log.error("[完结订单]更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultStatusEnum.ORDER_UPDATE_FAIL);
        }

        /**
         * 返还库存
         */
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetails())) {
            log.error("[完结订单]订单中无商品详情, orderDTO={}", orderDTO);
            throw new SellException(ResultStatusEnum.ORDER_DETAIL_EMPTY);
        }

        /**
         * 如果未支付, 抛出异常
         */
        if(!orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            //TODO
        }

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        /**
         * 判断订单状态
         */
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("[支付订单]订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultStatusEnum.ORDER_STATUS_ERROR);
        }

        /**
         * 判断支付状态
         */
        if(!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("[支付订单]支付状态不正确, orderDTO={}", orderDTO);
            throw new SellException(ResultStatusEnum.ORDER_PAY_STATUS_ERROR);
        }

        /**
         * 修改支付状态
         */
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = OrderMasterOrderDTOConverter.reverseConvert(orderDTO);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if(updateResult == null) {
            log.error("[支付订单]更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultStatusEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;
    }
}
