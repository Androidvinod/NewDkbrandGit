
package com.dolphin.zanders.Model.ProductlistModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCategoryProductlist {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Count")
    @Expose
    private String count;

    @SerializedName("product")
    @Expose
    //availability
    private List<GetCategoryProductlistInnerData> product = null;

    @SerializedName("is_last")
    @Expose
    private Integer isLast;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<GetCategoryProductlistInnerData> getProduct() {
        return product;
    }

    public void setProduct(List<GetCategoryProductlistInnerData> product) {
        this.product = product;
    }

    public Integer getIsLast() {
        return isLast;
    }

    public void setIsLast(Integer isLast) {
        this.isLast = isLast;
    }

}
