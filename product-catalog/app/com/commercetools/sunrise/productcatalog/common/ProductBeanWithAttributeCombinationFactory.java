package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.utils.AttributeFormatter;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.Attribute;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

@RequestScoped
public class ProductBeanWithAttributeCombinationFactory extends ViewModelFactory {

    private final Locale locale;
    private final ProductDataConfig productDataConfig;
    private final ProductReverseRouter productReverseRouter;
    private final AttributeFormatter attributeFormatter;

    @Inject
    public ProductBeanWithAttributeCombinationFactory(final Locale locale, final ProductDataConfig productDataConfig,
                                                      final ProductReverseRouter productReverseRouter, final AttributeFormatter attributeFormatter) {
        this.locale = locale;
        this.productDataConfig = productDataConfig;
        this.productReverseRouter = productReverseRouter;
        this.attributeFormatter = attributeFormatter;
    }

    public void fill(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        initialize(bean, product, variant);
    }

    protected final void initialize(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        fillVariantIdentifiers(bean, product, variant);
        fillVariants(bean, product, variant);
    }

    protected void fillVariants(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        final Map<String, ProductVariantReferenceBean> variantsMap = new HashMap<>();
        product.getAllVariants()
                .forEach(productVariant -> variantsMap.put(createAttributeCombination(productVariant), createVariantReference(product, productVariant)));
        bean.setVariants(variantsMap);
    }

    protected void fillVariantIdentifiers(final ProductBean bean, final ProductProjection product, final ProductVariant variant) {
        bean.setVariantIdentifiers(productDataConfig.getSelectableAttributes());
    }

    private ProductVariantReferenceBean createVariantReference(final ProductProjection product, final ProductVariant variant) {
        final ProductVariantReferenceBean bean = new ProductVariantReferenceBean();
        bean.setId(variant.getId());
        bean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(locale, product, variant));
        return bean;
    }

    private String createAttributeCombination(final ProductVariant variant) {
        return productDataConfig.getSelectableAttributes().stream()
                .map(selectableAttr -> createAttributeKey(variant.getAttribute(selectableAttr)))
                .collect(joining("-"));
    }

    private String createAttributeKey(@Nullable final Attribute nullableAttribute) {
        return Optional.ofNullable(nullableAttribute)
                .map(attributeFormatter::valueAsKey)
                .orElse("");
    }

}
