package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.viewmodels.ViewModel;

public class ProductViewModel extends ViewModel {

    private String productId;
    private int variantId;
    private ProductGalleryViewModel gallery;
    private ProductVariantViewModel variant;
    private String availability;

    public ProductViewModel() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(final String productId) {
        this.productId = productId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(final int variantId) {
        this.variantId = variantId;
    }

    public ProductGalleryViewModel getGallery() {
        return gallery;
    }

    public void setGallery(final ProductGalleryViewModel gallery) {
        this.gallery = gallery;
    }

    public ProductVariantViewModel getVariant() {
        return variant;
    }

    public void setVariant(final ProductVariantViewModel variant) {
        this.variant = variant;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(final String availability) {
        this.availability = availability;
    }
}
