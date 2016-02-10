package productcatalog.common;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.models.ProductDataConfig;
import common.models.ProductVariantBean;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static common.utils.ProductAttributeUtils.attributeValue;
import static common.utils.ProductAttributeUtils.attributeValueAsKey;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class ProductBean extends Base {
    // TODO ratingX
    // TODO delivery
    private String productId;
    private int variantId;
    private String description;
    private ProductGalleryData gallery;
    private List<SelectableProductAttributeBean> attributes;
    private ProductVariantBean variant;
    private Map<String, String> variants;
    private List<String> variantIdentifiers;
    private ProductDetailsBean details;

    public ProductBean() {
    }

    public ProductBean(final ProductProjection product, final ProductVariant variant, final ProductDataConfig productDataConfig,
                       final UserContext userContext, final ReverseRouter reverseRouter) {
        this.productId = product.getId();
        this.variantId = variant.getId();
        this.description = Optional.ofNullable(product.getDescription())
                .flatMap(locText -> locText.find(userContext.locales()))
                .orElse("");
        this.gallery = new ProductGalleryData(variant);
        this.attributes = productDataConfig.getSelectableAttributes().stream()
                .map(variant::getAttribute)
                .filter(attr -> attr != null)
                .map(attr -> new SelectableProductAttributeBean(attr, product, productDataConfig, userContext))
                .collect(toList());
        this.variant = new ProductVariantBean(product, variant, userContext, reverseRouter);
        this.variants = createVariantsMap(product, productDataConfig, userContext);
        this.variantIdentifiers = productDataConfig.getSelectableAttributes();
        this.details = new ProductDetailsBean(variant, productDataConfig, userContext);
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

    public ProductGalleryData getGallery() {
        return gallery;
    }

    public void setGallery(final ProductGalleryData gallery) {
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

    public ProductDetailsBean getDetails() {
        return details;
    }

    public void setDetails(final ProductDetailsBean details) {
        this.details = details;
    }

    private static Map<String, String> createVariantsMap(final ProductProjection product, final ProductDataConfig productDataConfig,
                                                         final UserContext userContext) {
        final Map<String, String> variantsMap = new HashMap<>();
        product.getAllVariants().forEach(variant -> {
            final String attrCombination = productDataConfig.getSelectableAttributes().stream()
                    .map(variant::getAttribute)
                    .filter(enabledAttr -> enabledAttr != null)
                    .map(enabledAttr -> {
                        final String enabledAttrValue = attributeValue(enabledAttr, productDataConfig.getMetaProductType(), userContext);
                        return attributeValueAsKey(enabledAttrValue);
                    })
                    .collect(joining("-"));
            variantsMap.put(attrCombination, variant.getId().toString());
        });
        return variantsMap;
    }
}
