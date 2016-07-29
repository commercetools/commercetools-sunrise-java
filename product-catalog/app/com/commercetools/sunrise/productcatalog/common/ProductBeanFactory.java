package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.models.ProductAttributeBeanFactory;
import com.commercetools.sunrise.common.models.ProductVariantBeanFactory;
import com.commercetools.sunrise.common.models.SelectableProductAttributeBean;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.Image;
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
        final ProductBean bean = new ProductBean();
        initialize(bean, product, variant);
        return bean;
    }

    protected final void initialize(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        fillIds(bean, product, variant);
        fillDescription(bean, product, variant);
        fillGallery(bean, product, variant);
        fillDetails(bean, product, variant);
        fillVariant(bean, product, variant);
        fillVariantIdentifiers(bean, product, variant);
        fillVariants(bean, product, variant);
        fillAttributes(bean, product, variant);
    }

    protected void fillAttributes(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        bean.setAttributes(createSelectableAttributes(product, variant));
    }

    protected void fillVariants(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        bean.setVariants(createAttributeCombinationToVariantMap(product));
    }

    protected void fillVariantIdentifiers(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        bean.setVariantIdentifiers(productDataConfig.getSelectableAttributes());
    }

    protected void fillVariant(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        bean.setVariant(productVariantBeanFactory.create(product, variant));
    }

    protected void fillDetails(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        bean.setDetails(createProductDetails(product, variant));
    }

    protected void fillGallery(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        bean.setGallery(createGallery(product, variant));
    }

    protected void fillDescription(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        bean.setDescription(Optional.ofNullable(product.getDescription())
                .flatMap(locText -> locText.find(userContext.locales()))
                .orElse(""));
    }

    protected void fillIds(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        bean.setProductId(product.getId());
        bean.setVariantId(variant.getId());
    }

    protected ProductGalleryBean createGallery(final ProductProjection product, final ProductVariant variant) {
        final ProductGalleryBean bean = new ProductGalleryBean();
        bean.setList(variant.getImages().stream()
                .map(this::createProductImage)
                .collect(toList()));
        return bean;
    }

    protected ProductDetailsBean createProductDetails(final ProductProjection product, final ProductVariant variant) {
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

    protected ProductImageBean createProductImage(final Image image) {
        final ProductImageBean imageBean = new ProductImageBean();
        imageBean.setThumbImage(image.getUrl());
        imageBean.setBigImage(image.getUrl());
        return imageBean;
    }
}
