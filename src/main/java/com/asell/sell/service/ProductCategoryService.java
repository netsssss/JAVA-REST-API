package com.asell.sell.service;

import com.asell.sell.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {

    /**
     * 根据ID查询一个类目
     * @param categoryId
     * @return
     */
    ProductCategory findById(Integer categoryId);

    /**
     * 查询全部类目
     * @return
     */
    List<ProductCategory> findAll();

    /**
     * 根据多个类目编号查询类目
     * @param categoryTypeList
     * @return
     */
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    /**
     * 添加和修改类目
     * @param productCategory
     * @return
     */
    ProductCategory save(ProductCategory productCategory);
}
