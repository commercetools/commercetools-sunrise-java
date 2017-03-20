package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;
import com.commercetools.sunrise.framework.viewmodels.content.products.ProductVariantViewModel;
import io.sphere.sdk.models.LocalizedString;

import java.util.List;

public class ProductViewModel extends ViewModel {

    private String productId;
    private int variantId;
    private LocalizedString description;
    private ProductGalleryViewModel gallery;
    private List<SelectableProductAttributeViewModel> attributes;
    private ProductVariantViewModel variant;
    private ProductVariantReferenceMapViewModel variants;
    private List<String> variantIdentifiers;
    private ProductDetailsViewModel details;
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

    public LocalizedString getDescription() {
        return description;
    }

    public void setDescription(final LocalizedString description) {
        this.description = description;
    }

    public ProductGalleryViewModel getGallery() {
        return gallery;
    }

    public void setGallery(final ProductGalleryViewModel gallery) {
        this.gallery = gallery;
    }

    public List<SelectableProductAttributeViewModel> getAttributes() {
        return attributes;
    }

    public void setAttributes(final List<SelectableProductAttributeViewModel> attributes) {
        this.attributes = attributes;
    }

    public ProductVariantViewModel getVariant() {
        return variant;
    }

    public void setVariant(final ProductVariantViewModel variant) {
        this.variant = variant;
    }

    public ProductVariantReferenceMapViewModel getVariants() {
        return variants;
    }

    public void setVariants(final ProductVariantReferenceMapViewModel variants) {
        this.variants = variants;
    }

    public List<String> getVariantIdentifiers() {
        return variantIdentifiers;
    }

    public void setVariantIdentifiers(final List<String> variantIdentifiers) {
        this.variantIdentifiers = variantIdentifiers;
    }

    public ProductDetailsViewModel getDetails() {
        return details;
    }

    public void setDetails(final ProductDetailsViewModel details) {
        this.details = details;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(final String availability) {
        this.availability = availability;
    }
}
