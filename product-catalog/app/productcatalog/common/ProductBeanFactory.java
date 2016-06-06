package productcatalog.common;

import common.contexts.UserContext;
import common.models.ProductAttributeBeanFactory;
import common.models.ProductDataConfig;
import common.models.ProductVariantBeanFactory;
import common.models.SelectableProductAttributeBean;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.Attribute;
import wedecidelatercommon.ProductReverseRouter;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static common.utils.ProductAttributeUtils.attributeValue;
import static common.utils.ProductAttributeUtils.attributeValueAsKey;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class ProductBeanFactory {

    @Inject
    private UserContext userContext;
    @Inject
    private ProductDataConfig productDataConfig;
    @Inject
    private ProductReverseRouter productReverseRouter;
    @Inject
    private ProductVariantBeanFactory productVariantBeanFactory;
    @Inject
    private ProductAttributeBeanFactory productAttributeBeanFactory;

    public ProductBean create(final ProductProjection product, final ProductVariant variant) {
        final ProductBean productBean = new ProductBean();
        productBean.setProductId(product.getId());
        productBean.setVariantId(variant.getId());
        productBean.setDescription(createDescription(product));
        productBean.setGallery(createGallery(variant));
        productBean.setDetails(createProductDetails(variant));
        productBean.setVariant(productVariantBeanFactory.create(product, variant));
        productBean.setVariantIdentifiers(productDataConfig.getSelectableAttributes());
        productBean.setVariants(createAttributeCombinationToVariantMap(product));
        productBean.setAttributes(createSelectableAttributes(product, variant));
        return productBean;
    }

    private String createDescription(final ProductProjection product) {
        return Optional.ofNullable(product.getDescription())
                .flatMap(locText -> locText.find(userContext.locales()))
                .orElse("");
    }

    private ProductGalleryBean createGallery(final ProductVariant variant) {
        final ProductGalleryBean bean = new ProductGalleryBean();
        bean.setList(variant.getImages().stream()
                .map(ProductImageData::new)
                .collect(toList()));
        return bean;
    }

    private ProductDetailsBean createProductDetails(final ProductVariant variant) {
        final ProductDetailsBean bean = new ProductDetailsBean();
        bean.setFeatures(productDataConfig.getDisplayedAttributes().stream()
                .map(variant::getAttribute)
                .filter(attr -> attr != null)
                .map(attr -> productAttributeBeanFactory.create(attr))
                .collect(toList()));
        return bean;
    }

    private List<SelectableProductAttributeBean> createSelectableAttributes(final ProductProjection product, final ProductVariant variant) {
        return productDataConfig.getSelectableAttributes().stream()
                .map(variant::getAttribute)
                .filter(attr -> attr != null)
                .map(attr -> productAttributeBeanFactory.createSelectableAttribute(attr, product))
                .collect(toList());
    }

    private Map<String, ProductVariantReferenceBean> createAttributeCombinationToVariantMap(final ProductProjection product) {
        final Map<String, ProductVariantReferenceBean> variantsMap = new HashMap<>();
        product.getAllVariants().forEach(variant -> {
            final String attrCombination = productDataConfig.getSelectableAttributes().stream()
                    .map(selectableAttr -> createAttributeKey(variant.getAttribute(selectableAttr)))
                    .collect(joining("-"));
            variantsMap.put(attrCombination, createVariantReference(product, variant));
        });
        return variantsMap;
    }

    private String createAttributeKey(@Nullable final Attribute nullableAttribute) {
        return Optional.ofNullable(nullableAttribute)
                .map(attribute -> {
                    final String enabledAttrValue = attributeValue(attribute, userContext.locales(), productDataConfig.getMetaProductType());
                    return attributeValueAsKey(enabledAttrValue);
                })
                .orElse("");
    }

    private ProductVariantReferenceBean createVariantReference(final ProductProjection product, final ProductVariant variant) {
        final ProductVariantReferenceBean bean = new ProductVariantReferenceBean();
        bean.setId(variant.getId());
        bean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(userContext.locale(), product, variant));
        return bean;
    }
}
