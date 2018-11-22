package com.asell.sell.viewobject;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 返回商品格式(包含类目)
 */
@Data
public class ProductViewObject {

    /**
     * 类目名字
     */
    @JsonProperty("name")
    private String categoryName;

    /**
     * 类目编号
     */
    @JsonProperty("type")
    private Integer categoryType;

    /**
     * 商品信息
     */
    @JsonProperty("foods")
    private List<ProductInfoViewObject> productInfoViewObjects;
}
