
package com.dolphin.zanders.Model.NewOrderModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payment {

    @SerializedName("account_status")
    @Expose
    private Object accountStatus;
    @SerializedName("additional_information")
    @Expose
    private List<Object> additionalInformation = null;
    @SerializedName("cc_last4")
    @Expose
    private Object ccLast4;
    @SerializedName("entity_id")
    @Expose
    private Integer entityId;
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("parent_id")
    @Expose
    private Integer parentId;

    public Object getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Object accountStatus) {
        this.accountStatus = accountStatus;
    }

    public List<Object> getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(List<Object> additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public Object getCcLast4() {
        return ccLast4;
    }

    public void setCcLast4(Object ccLast4) {
        this.ccLast4 = ccLast4;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

}
