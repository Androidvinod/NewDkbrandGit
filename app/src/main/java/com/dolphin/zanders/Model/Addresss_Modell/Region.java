
package com.dolphin.zanders.Model.Addresss_Modell;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Region {

    @SerializedName("region_code")
    @Expose
    private String regionCode;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("region_id")
    @Expose
    private Integer regionId;

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

}
