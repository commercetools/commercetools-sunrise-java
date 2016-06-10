package com.commercetools.sunrise.productcatalog.common;

import common.models.ProductVariantBean;
import common.models.SelectableProductAttributeBean;
import io.sphere.sdk.models.Base;

import java.util.List;
import java.util.Map;

public class ProductBean extends Base {
    // TODO ratingX
    // TODO delivery
    private String productId;
    private int variantId;
    private String description;
    private ProductGalleryBean gallery;
    private List<SelectableProductAttributeBean> attributes;
    private ProductVariantBean variant;
    private Map<String, ProductVariantReferenceBean> variants;
    private List<String> variantIdentifiers;
    private ProductDetailsBean details;

    public ProductBean() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public ProductGalleryBean getGallery() {
        return gallery;
    }

    public void setGallery(final ProductGalleryBean gallery) {
        this.gallery = gallery;
    }

    public List<SelectableProductAttributeBean> getAttributes() {
        return attributes;
    }

    public void setAttributes(final List<SelectableProductAttributeBean> attributes) {
        this.attributes = attributes;
    }

    public ProductVariantBean getVariant() {
        return variant;
    }

    public void setVariant(final ProductVariantBean variant) {
        this.variant = variant;
    }

    public Map<String, ProductVariantReferenceBean> getVariants() {
        return variants;
    }

    public void setVariants(final Map<String, ProductVariantReferenceBean> variants) {
        this.variants = variants;
    }

    public List<String> getVariantIdentifiers() {
        return variantIdentifiers;
    }

    public void setVariantIdentifiers(final List<String> variantIdentifiers) {
        this.variantIdentifiers = variantIdentifiers;
    }

    public ProductDetailsBean getDetails() {
        return details;
    }

    public void setDetails(final ProductDetailsBean details) {
        this.details = details;
    }
}
