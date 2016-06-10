package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.models.ProductAttributeBeanFactoryInjectless;
import com.commercetools.sunrise.common.models.ProductDataConfig;
import com.commercetools.sunrise.common.models.ProductVariantBeanFactoryInjectless;
import com.commercetools.sunrise.common.models.SelectableProductAttributeBean;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.Attribute;
import com.commercetools.sunrise.wedecidelatercommon.ProductReverseRouter;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.commercetools.sunrise.common.utils.ProductAttributeUtils.attributeValue;
import static com.commercetools.sunrise.common.utils.ProductAttributeUtils.attributeValueAsKey;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class ProductBeanFactoryInjectless {

    public static ProductBean create(final ProductProjection product, final ProductVariant variant, final UserContext userContext,
                              final ProductDataConfig productDataConfig, final ProductReverseRouter productReverseRouter) {
        final ProductBean productBean = new ProductBean();
        productBean.setProductId(product.getId());
        productBean.setVariantId(variant.getId());
        productBean.setDescription(createDescription(product, userContext));
        productBean.setGallery(createGallery(variant));
        productBean.setDetails(createProductDetails(variant, userContext, productDataConfig));
        productBean.setVariant(ProductVariantBeanFactoryInjectless.create(product, variant, userContext, productReverseRouter));
        productBean.setVariantIdentifiers(productDataConfig.getSelectableAttributes());
        productBean.setVariants(createAttributeCombinationToVariantMap(product, userContext, productDataConfig, productReverseRouter));
        productBean.setAttributes(createSelectableAttributes(product, variant, userContext, productDataConfig));
        return productBean;
    }

    private static String createDescription(final ProductProjection product, final UserContext userContext) {
        return Optional.ofNullable(product.getDescription())
                .flatMap(locText -> locText.find(userContext.locales()))
                .orElse("");
    }

    private static ProductGalleryBean createGallery(final ProductVariant variant) {
        final ProductGalleryBean bean = new ProductGalleryBean();
        bean.setList(variant.getImages().stream()
                .map(ProductImageData::new)
                .collect(toList()));
        return bean;
    }

    private static ProductDetailsBean createProductDetails(final ProductVariant variant, final UserContext userContext, final ProductDataConfig productDataConfig) {
        final ProductDetailsBean bean = new ProductDetailsBean();
        bean.setFeatures(productDataConfig.getDisplayedAttributes().stream()
                .map(variant::getAttribute)
                .filter(attr -> attr != null)
                .map(attr -> ProductAttributeBeanFactoryInjectless.create(attr, userContext, productDataConfig))
                .collect(toList()));
        return bean;
    }

    private static List<SelectableProductAttributeBean> createSelectableAttributes(final ProductProjection product,
                                                                            final ProductVariant variant, final UserContext userContext, final ProductDataConfig productDataConfig) {
        return productDataConfig.getSelectableAttributes().stream()
                .map(variant::getAttribute)
                .filter(attr -> attr != null)
                .map(attr -> ProductAttributeBeanFactoryInjectless.createSelectableAttribute(attr, product, userContext, productDataConfig))
                .collect(toList());
    }

    private static Map<String, ProductVariantReferenceBean> createAttributeCombinationToVariantMap(final ProductProjection product,
                                                                                            final UserContext userContext,
                                                                                            final ProductDataConfig productDataConfig,
                                                                                            final ProductReverseRouter productReverseRouter) {
        final Map<String, ProductVariantReferenceBean> variantsMap = new HashMap<>();
        product.getAllVariants().forEach(variant -> {
            final String attrCombination = productDataConfig.getSelectableAttributes().stream()
                    .map(selectableAttr -> createAttributeKey(variant.getAttribute(selectableAttr), userContext, productDataConfig))
                    .collect(joining("-"));
            variantsMap.put(attrCombination, createVariantReference(product, variant, userContext, productReverseRouter));
        });
        return variantsMap;
    }

    private static String createAttributeKey(@Nullable final Attribute nullableAttribute, final UserContext userContext,
                                      final ProductDataConfig productDataConfig) {
        return Optional.ofNullable(nullableAttribute)
                .map(attribute -> {
                    final String enabledAttrValue = attributeValue(attribute, userContext.locales(), productDataConfig.getMetaProductType());
                    return attributeValueAsKey(enabledAttrValue);
                })
                .orElse("");
    }

    private static ProductVariantReferenceBean createVariantReference(final ProductProjection product, final ProductVariant variant,
                                                               final UserContext userContext, final ProductReverseRouter productReverseRouter) {
        final ProductVariantReferenceBean bean = new ProductVariantReferenceBean();
        bean.setId(variant.getId());
        bean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(userContext.locale(), product, variant));
        return bean;
    }
}
