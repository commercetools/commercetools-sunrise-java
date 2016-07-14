package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.models.ProductAttributeBeanFactory;
import com.commercetools.sunrise.common.models.ProductVariantBeanFactory;
import com.commercetools.sunrise.common.models.SelectableProductAttributeBean;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.Attribute;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.commercetools.sunrise.common.utils.ProductAttributeUtils.attributeValue;
import static com.commercetools.sunrise.common.utils.ProductAttributeUtils.attributeValueAsKey;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class ProductBeanFactory extends Base {

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
        return fillBean(new ProductBean(), product, variant);
    }

    protected <T extends ProductBean> T fillBean(final T productBean, final ProductProjection product, final ProductVariant variant) {
        fillIds(product, variant, productBean);
        fillDescription(product, productBean);
        fillGallery(variant, productBean);
        fillDetails(variant, productBean);
        fillVariant(product, variant, productBean);
        fillVariantIdentifiers(productBean);
        fillVariants(product, productBean);
        fillAttributes(product, variant, productBean);
        return productBean;
    }

    protected void fillAttributes(final ProductProjection product, final ProductVariant variant, final ProductBean productBean) {
        productBean.setAttributes(createSelectableAttributes(product, variant));
    }

    protected void fillVariants(final ProductProjection product, final ProductBean productBean) {
        productBean.setVariants(createAttributeCombinationToVariantMap(product));
    }

    protected void fillVariantIdentifiers(final ProductBean productBean) {
        productBean.setVariantIdentifiers(productDataConfig.getSelectableAttributes());
    }

    protected void fillVariant(final ProductProjection product, final ProductVariant variant, final ProductBean productBean) {
        productBean.setVariant(productVariantBeanFactory.create(product, variant));
    }

    protected void fillDetails(final ProductVariant variant, final ProductBean productBean) {
        productBean.setDetails(createProductDetails(variant));
    }

    protected void fillGallery(final ProductVariant variant, final ProductBean productBean) {
        productBean.setGallery(createGallery(variant));
    }

    protected void fillDescription(final ProductProjection product, final ProductBean productBean) {
        productBean.setDescription(createDescription(product));
    }

    protected void fillIds(final ProductProjection product, final ProductVariant variant, final ProductBean productBean) {
        productBean.setProductId(product.getId());
        productBean.setVariantId(variant.getId());
    }

    protected String createDescription(final ProductProjection product) {
        return Optional.ofNullable(product.getDescription())
                .flatMap(locText -> locText.find(userContext.locales()))
                .orElse("");
    }

    protected ProductGalleryBean createGallery(final ProductVariant variant) {
        final ProductGalleryBean bean = new ProductGalleryBean();
        bean.setList(variant.getImages().stream()
                .map(ProductImageBean::new)
                .collect(toList()));
        return bean;
    }

    protected ProductDetailsBean createProductDetails(final ProductVariant variant) {
        final ProductDetailsBean bean = new ProductDetailsBean();
        bean.setFeatures(productDataConfig.getDisplayedAttributes().stream()
                .map(variant::getAttribute)
                .filter(attr -> attr != null)
                .map(attr -> productAttributeBeanFactory.create(attr))
                .collect(toList()));
        return bean;
    }

    protected List<SelectableProductAttributeBean> createSelectableAttributes(final ProductProjection product, final ProductVariant variant) {
        return productDataConfig.getSelectableAttributes().stream()
                .map(variant::getAttribute)
                .filter(attr -> attr != null)
                .map(attr -> productAttributeBeanFactory.createSelectableAttribute(attr, product))
                .collect(toList());
    }

    protected Map<String, ProductVariantReferenceBean> createAttributeCombinationToVariantMap(final ProductProjection product) {
        final Map<String, ProductVariantReferenceBean> variantsMap = new HashMap<>();
        product.getAllVariants().forEach(variant -> {
            final String attrCombination = productDataConfig.getSelectableAttributes().stream()
                    .map(selectableAttr -> createAttributeKey(variant.getAttribute(selectableAttr)))
                    .collect(joining("-"));
            variantsMap.put(attrCombination, createVariantReference(product, variant));
        });
        return variantsMap;
    }

    protected String createAttributeKey(@Nullable final Attribute nullableAttribute) {
        return Optional.ofNullable(nullableAttribute)
                .map(attribute -> {
                    final String enabledAttrValue = attributeValue(attribute, userContext.locales(), productDataConfig.getMetaProductType());
                    return attributeValueAsKey(enabledAttrValue);
                })
                .orElse("");
    }

    protected ProductVariantReferenceBean createVariantReference(final ProductProjection product, final ProductVariant variant) {
        final ProductVariantReferenceBean bean = new ProductVariantReferenceBean();
        bean.setId(variant.getId());
        bean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(userContext.locale(), product, variant));
        return bean;
    }
}
