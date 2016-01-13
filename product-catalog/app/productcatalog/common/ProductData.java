package productcatalog.common;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.models.ProductDataConfig;
import common.models.ProductVariantBean;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import java.util.List;
import java.util.Map;

public class ProductData extends Base {
    // TODO ratingX
    // TODO details
    private GalleryData gallery;
    private ProductVariantBean variant;
    private Map<String, String> variants;
    private List<String> variantIdentifiers;

    public ProductData() {
    }

    public ProductData(final ProductProjection product, final ProductVariant variant, final ProductDataConfig productDataConfig,
                       final UserContext userContext, final ReverseRouter reverseRouter) {
        this.gallery = new GalleryData(variant);
        this.variant = new ProductVariantBean(product, variant, productDataConfig, userContext, reverseRouter);
        this.variantIdentifiers = productDataConfig.getAttributeWhiteList();
    }

    public GalleryData getGallery() {
        return gallery;
    }

    public void setGallery(final GalleryData gallery) {
        this.gallery = gallery;
    }

    public ProductVariantBean getVariant() {
        return variant;
    }

    public void setVariant(final ProductVariantBean variant) {
        this.variant = variant;
    }

    public Map<String, String> getVariants() {
        return variants;
    }

    public void setVariants(final Map<String, String> variants) {
        this.variants = variants;
    }

    public List<String> getVariantIdentifiers() {
        return variantIdentifiers;
    }

    public void setVariantIdentifiers(final List<String> variantIdentifiers) {
        this.variantIdentifiers = variantIdentifiers;
    }
}
