
package com.dolphin.zanders.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewShippingModel {

    @SerializedName("carrier_code")
    @Expose
    private String carrierCode;
    @SerializedName("method_code")
    @Expose
    private String methodCode;
    @SerializedName("carrier_title")
    @Expose
    private String carrierTitle;
    @SerializedName("method_title")
    @Expose
    private String methodTitle;

    public NewShippingModel(String carrierCode, String methodCode, String carrierTitle,
                            String methodTitle,
                             String errorMessage
                          ) {
        this.carrierCode = carrierCode;
        this.methodCode = methodCode;
        this.carrierTitle = carrierTitle;
        this.methodTitle = methodTitle;

        this.baseAmount = baseAmount;

        this.errorMessage = errorMessage;
    }

    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("base_amount")
    @Expose
    private Integer baseAmount;
    @SerializedName("available")
    @Expose
    private Boolean available;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("price_excl_tax")
    @Expose
    private Integer priceExclTax;
    @SerializedName("price_incl_tax")
    @Expose
    private Integer priceInclTax;

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getMethodCode() {
        return methodCode;
    }

    public void setMethodCode(String methodCode) {
        this.methodCode = methodCode;
    }

    public String getCarrierTitle() {
        return carrierTitle;
    }

    public void setCarrierTitle(String carrierTitle) {
        this.carrierTitle = carrierTitle;
    }

    public String getMethodTitle() {
        return methodTitle;
    }

    public void setMethodTitle(String methodTitle) {
        this.methodTitle = methodTitle;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(Integer baseAmount) {
        this.baseAmount = baseAmount;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getPriceExclTax() {
        return priceExclTax;
    }

    public void setPriceExclTax(Integer priceExclTax) {
        this.priceExclTax = priceExclTax;
    }

    public Integer getPriceInclTax() {
        return priceInclTax;
    }

    public void setPriceInclTax(Integer priceInclTax) {
        this.priceInclTax = priceInclTax;
    }

}
