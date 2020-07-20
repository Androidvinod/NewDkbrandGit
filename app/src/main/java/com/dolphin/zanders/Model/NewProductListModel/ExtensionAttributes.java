
package com.dolphin.zanders.Model.NewProductListModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtensionAttributes {

    @SerializedName("website_ids")
    @Expose
    private List<Integer> websiteIds = null;
    @SerializedName("category_links")
    @Expose
    private List<CategoryLink> categoryLinks = null;

    public List<Integer> getWebsiteIds() {
        return websiteIds;
    }

    public void setWebsiteIds(List<Integer> websiteIds) {
        this.websiteIds = websiteIds;
    }

    public List<CategoryLink> getCategoryLinks() {
        return categoryLinks;
    }

    public void setCategoryLinks(List<CategoryLink> categoryLinks) {
        this.categoryLinks = categoryLinks;
    }

}
