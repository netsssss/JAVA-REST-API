package com.asell.sell.service.impl;

import com.asell.sell.dto.CartDTO;
import com.asell.sell.entity.ProductInfo;
import com.asell.sell.enums.ProductStatusEnum;
import com.asell.sell.enums.ResultStatusEnum;
import com.asell.sell.exception.SellException;
import com.asell.sell.repository.ProductInfoRepository;
import com.asell.sell.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    private ProductInfoRepository repository;

    @Override
    public ProductInfo findById(String productId) {
        return repository.findById(productId).get();
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    @Override
    @Transactional
    public void increaseStock(List<CartDTO> cartDTOS) {
        /**
         * 遍历购物车
         */
        for (CartDTO cartDTO : cartDTOS) {
            /**
             * 按购物车中的商品id查询商品
             */
            ProductInfo productInfo = repository.findById(cartDTO.getProductId()).get();

            /**
             * 判断商品是否存在
             */
            if(productInfo == null) {
                throw new SellException(ResultStatusEnum.PRODUCT_NOT_EXIST);
            }

            /**
             * 当前库存加购买数量
             */
            Integer result = productInfo.getProductStock() + cartDTO.getProductQuantity();

            /**
             * 更新库存
             */
            productInfo.setProductStock(result);

            repository.save(productInfo);
        }
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOS) {
        /**
         * 遍历购物车
         */
        for (CartDTO cartDTO : cartDTOS) {
            /**
             * 按购物车中的商品id查询商品
             */
            ProductInfo productInfo = repository.findById(cartDTO.getProductId()).get();

            /**
             * 判断商品是否存在
             */
            if(productInfo == null) {
                throw new SellException(ResultStatusEnum.PRODUCT_NOT_EXIST);
            }

            /**
             * 当前库存减购买数量
             */
            Integer result = productInfo.getProductStock() - cartDTO.getProductQuantity();

            /**
             * 判断是否超卖
             */
            if(result < 0) {
                throw new SellException(ResultStatusEnum.PRODUCT_STOCK_ERROR);
            }

            /**
             * 更新库存
             */
            productInfo.setProductStock(result);

            repository.save(productInfo);
        }
    }
}
