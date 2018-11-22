package com.asell.sell.controller;

import com.asell.sell.Utils.ResultViewObjectUtil;
import com.asell.sell.converter.OrderFormOrderDTOConverter;
import com.asell.sell.dto.OrderDTO;
import com.asell.sell.enums.ResultStatusEnum;
import com.asell.sell.exception.SellException;
import com.asell.sell.form.OrderForm;
import com.asell.sell.service.BuyerService;
import com.asell.sell.service.OrderService;
import com.asell.sell.viewobject.ResultViewObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单
 */
@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    /**
     * 创建订单
     */
    @PostMapping("/create")
    public ResultViewObject<Map<String, String>> create(@Valid OrderForm orderForm,
                                                        BindingResult bindingResult) {
        System.out.println(orderForm.toString());
        if (bindingResult.hasErrors()) {
            log.error("[创建订单] 参数不正确, orderFrom={}", orderForm);
            throw new SellException(ResultStatusEnum.PARAMS_ERROR.getCode(),
                                    bindingResult.getFieldError().getDefaultMessage());
        }

        OrderDTO orderDTO = OrderFormOrderDTOConverter.convert(orderForm);

        if (CollectionUtils.isEmpty(orderDTO.getOrderDetails())) {
            log.error("[创建订单] 购物车不能为空");
            throw new SellException(ResultStatusEnum.CART_EMPTY);
        }

        OrderDTO createResult = orderService.create(orderDTO);

        Map<String, String> map = new HashMap<>();

        map.put("orderId", createResult.getOrderId());

        ResultViewObjectUtil<Map<String, String>> resultViewObjectUtil = new ResultViewObjectUtil<Map<String, String>>();
        return resultViewObjectUtil.success(map);
    }

    /**
     * 订单列表
     */
    @GetMapping("/list")
    public ResultViewObject<List<OrderDTO>> list(@RequestParam("openid") String openid,
                                                 @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                 @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if (StringUtils.isEmpty(openid)) {
            log.error("[订单列表]openid为空");
            throw new SellException(ResultStatusEnum.PARAMS_ERROR);
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<OrderDTO> orderDTOPage = orderService.findList(openid, pageable);

        ResultViewObjectUtil<List<OrderDTO>> resultViewObjectUtil = new ResultViewObjectUtil<List<OrderDTO>>();
        return  resultViewObjectUtil.success(orderDTOPage.getContent());

    }
    /**
     * 订单详情
     */
    @GetMapping("/detail")
    public ResultViewObject<OrderDTO> detail(@RequestParam("openid") String openid,
                                                 @RequestParam("orderId") String orderId) {
        OrderDTO orderDTO = buyerService.findOneByOrder(openid, orderId);
        ResultViewObjectUtil<OrderDTO> resultViewObjectUtil = new ResultViewObjectUtil<OrderDTO>();        
        return resultViewObjectUtil.success(orderDTO);
    }
    /**
     * 取消订单
     */
    @PostMapping("/cancel")
    public ResultViewObject<OrderDTO> cancel(@RequestParam("openid") String openid,
                                             @RequestParam("orderId") String orderId) {

        buyerService.findOneByOrder(openid, orderId);
        ResultViewObjectUtil<OrderDTO> resultViewObjectUtil = new ResultViewObjectUtil<OrderDTO>();
        return resultViewObjectUtil.success();
    }
}
