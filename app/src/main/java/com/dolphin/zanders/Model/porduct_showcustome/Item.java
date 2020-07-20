
package com.dolphin.zanders.Model.porduct_showcustome;

import java.util.List;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {
    private String isWishlisted;
    private String wishlist_item_id;
    private String image,tierprice,special_price;




    public Item(String isWishlisted, String wishlist_item_id, String image, Integer id, String sku, String name, Double price, String tierprice, String special_price) {
        this.isWishlisted = isWishlisted;
        this.wishlist_item_id = wishlist_item_id;
        this.image = image;
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.tierprice = tierprice;
        this.special_price = special_price;
    }


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("attribute_set_id")
    @Expose
    private Integer attributeSetId;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("visibility")
    @Expose
    private Integer visibility;
    @SerializedName("type_id")
    @Expose
    private String typeId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("extension_attributes")
    @Expose
    private ExtensionAttributes extensionAttributes;
    @SerializedName("product_links")
    @Expose
    private List<Object> productLinks = null;
    @SerializedName("options")
    @Expose
    private List<Object> options = null;
    @SerializedName("media_gallery_entries")
    @Expose
    private List<MediaGalleryEntry> mediaGalleryEntries = null;

    @SerializedName("tier_prices")
    @Expose
    private List<TierPrice> tierPrices = null;

    public List<TierPrice> getTierPrices() {
        return tierPrices;
    }

    public void setTierPrices(List<TierPrice> tierPrices) {
        this.tierPrices = tierPrices;
    }

    public void setWishlist_item_id(String wishlist_item_id) {
        this.wishlist_item_id = wishlist_item_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTierprice() {
        return tierprice;
    }

    public void setTierprice(String tierprice) {
        this.tierprice = tierprice;
    }

    public String getSpecial_price() {
        return special_price;
    }

    public void setSpecial_price(String special_price) {
        this.special_price = special_price;
    }
    public String getIsWishlisted() {
        return isWishlisted;
    }

    public void setIsWishlisted(String isWishlisted) {
        this.isWishlisted = isWishlisted;
    }

    public String getWishlist_item_id() {
        return wishlist_item_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAttributeSetId() {
        return attributeSetId;
    }

    public void setAttributeSetId(Integer attributeSetId) {
        this.attributeSetId = attributeSetId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ExtensionAttributes getExtensionAttributes() {
        return extensionAttributes;
    }

    public void setExtensionAttributes(ExtensionAttributes extensionAttributes) {
        this.extensionAttributes = extensionAttributes;
    }

    public List<Object> getProductLinks() {
        return productLinks;
    }

    public void setProductLinks(List<Object> productLinks) {
        this.productLinks = productLinks;
    }

    public List<Object> getOptions() {
        return options;
    }

    public void setOptions(List<Object> options) {
        this.options = options;
    }

    public List<MediaGalleryEntry> getMediaGalleryEntries() {
        return mediaGalleryEntries;
    }

    public void setMediaGalleryEntries(List<MediaGalleryEntry> mediaGalleryEntries) {
        this.mediaGalleryEntries = mediaGalleryEntries;
    }



}
