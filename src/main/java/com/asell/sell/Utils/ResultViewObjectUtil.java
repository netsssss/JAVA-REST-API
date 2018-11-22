package com.asell.sell.Utils;

import com.asell.sell.enums.ResultStatusEnum;
import com.asell.sell.viewobject.ResultViewObject;

/**
 * 返回格式工具
 */
public class ResultViewObjectUtil<T> {

    /**
     * 成功(List<ProductViewObject>)
     * @param productViewObjects
     * @return
     */
    public ResultViewObject<T> success(T productViewObjects) {
        ResultViewObject<T> resultViewObject = new ResultViewObject<T>();

        /**
         * code
         */
        resultViewObject.setCode(ResultStatusEnum.SUCCESS.getCode());

        /**
         * msg
         */
        resultViewObject.setMsg(ResultStatusEnum.SUCCESS.getMessage());

        /**
         * data
         */
        resultViewObject.setData(productViewObjects);

        return resultViewObject;
    }

    /**
     * 无返回值成功
     * @return
     */
    public ResultViewObject<T> success() {
        return success(null);
    }

    /**
     * 失败
     * @param code
     * @param msg
     * @return
     */
    public ResultViewObject<T> fail(Integer code, String msg) {
        ResultViewObject<T> resultViewObject = new ResultViewObject<T>();

        /**
         * code
         */
        resultViewObject.setCode(code);

        /**
         * msg
         */
        resultViewObject.setMsg(msg);

        return resultViewObject;
    }
}
