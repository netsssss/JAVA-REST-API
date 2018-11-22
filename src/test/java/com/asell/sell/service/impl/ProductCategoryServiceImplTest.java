package com.asell.sell.service.impl;

import com.asell.sell.entity.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryServiceImplTest {

    @Autowired
    private ProductCategoryServiceImpl productCategoryService;

    @Test
    public void findById() {
        ProductCategory productCategory = productCategoryService.findById(1);

        Assert.assertEquals(new Integer(1), productCategory.getCategoryId());
    }

    @Test
    public void findAll() {
        List<ProductCategory> productCategories = productCategoryService.findAll();
        Assert.assertNotEquals(0,productCategories);
    }

    @Test
    public void findByCategoryTypeIn() {
        List<Integer> list = Arrays.asList(1,2,3,4);
        List<ProductCategory> productCategories = productCategoryService.findByCategoryTypeIn(list);
        Assert.assertNotEquals(0,productCategories);
    }

    @Test
    public void save() {
        ProductCategory productCategory = new ProductCategory("女生专享",4);
        Assert.assertNotNull(productCategory);
    }
}