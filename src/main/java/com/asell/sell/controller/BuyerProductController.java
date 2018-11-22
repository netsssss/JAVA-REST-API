package com.asell.sell.controller;

import com.asell.sell.Utils.ResultViewObjectUtil;
import com.asell.sell.entity.ProductCategory;
import com.asell.sell.entity.ProductInfo;
import com.asell.sell.service.ProductCategoryService;
import com.asell.sell.service.ProductInfoService;
import com.asell.sell.viewobject.ProductInfoViewObject;
import com.asell.sell.viewobject.ProductViewObject;
import com.asell.sell.viewobject.ResultViewObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品
 */
@RestController
@RequestMapping(value = "/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 获得所有类目及上架商品
     * @return
     */
    @GetMapping(value = "/list")
    public ResultViewObject<List<ProductViewObject>> list() {
        /**
         * 查询所有上架商品
         */
        List<ProductInfo> productInfos = productInfoService.findUpAll();

        /**
         * 查询类目(一次性查询、使用了java8 lambda特性)
         */
        List<Integer> categoryTypes = productInfos.stream().map(e -> e.getCategoryType()).collect(Collectors.toList());
        List<ProductCategory> productCategories = productCategoryService.findByCategoryTypeIn(categoryTypes);

        /**
         * 数据拼装
         */

        //遍历查询到的类目
        List<ProductViewObject> productViewObjects = new ArrayList<>();
        for(ProductCategory productCategory : productCategories) {
            ProductViewObject productViewObject = new ProductViewObject();
            productViewObject.setCategoryType(productCategory.getCategoryType());
            productViewObject.setCategoryName(productCategory.getCategoryName());

            //遍历类目下的商品
            List<ProductInfoViewObject> productInfoViewObjects = new ArrayList<>();
            for(ProductInfo productInfo : productInfos) {
                if(productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
                    ProductInfoViewObject productInfoViewObject = new ProductInfoViewObject();
                    BeanUtils.copyProperties(productInfo, productInfoViewObject);

                    productInfoViewObjects.add(productInfoViewObject);
                }
            }

            //将商品信息存入商品返回对象
            productViewObject.setProductInfoViewObjects(productInfoViewObjects);

            productViewObjects.add(productViewObject);
        }

        ResultViewObjectUtil<List<ProductViewObject>> resultViewObjectUtil = new ResultViewObjectUtil<List<ProductViewObject>>();
        return resultViewObjectUtil.success(productViewObjects);
    }
}
