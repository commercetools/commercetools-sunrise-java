package productcatalog.models;

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
    //private GalleryData gallery;
    private ProductVariantBean variant;
    private Map<String, String> variants;
    private List<String> variantIdentifiers;

    public ProductData() {
    }

    public ProductData(final ProductProjection product, final ProductVariant variant, final ProductDataConfig productDataConfig,
                       final UserContext userContext, final ReverseRouter reverseRouter) {
        final String slug = product.getSlug().find(userContext.locale()).orElse("");
        //this.url = reverseRouter.product(userContext.locale().toLanguageTag(), slug, variant.getSku()).url();
        this.variant = new ProductVariantBean(product, variant, userContext, productDataConfig);
        this.variantIdentifiers = productDataConfig.getAttributeWhiteList();
        // TODO variants
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
