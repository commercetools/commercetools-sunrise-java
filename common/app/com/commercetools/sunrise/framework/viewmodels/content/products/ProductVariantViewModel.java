package com.commercetools.sunrise.framework.viewmodels.content.products;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;
import io.sphere.sdk.models.LocalizedString;

public class ProductVariantViewModel extends ViewModel {

    private LocalizedString name;
    private String sku;
    private String url;
    private String image;
    private String price;
    private String priceOld;

    public ProductVariantViewModel() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public LocalizedString getName() {
        return name;
    }

    public void setName(final LocalizedString name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(final String price) {
        this.price = price;
    }

    public String getPriceOld() {
        return priceOld;
    }

    public void setPriceOld(final String priceOld) {
        this.priceOld = priceOld;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(final String sku) {
        this.sku = sku;
    }
}
