package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.utils.AttributeFormatter;
import io.sphere.sdk.models.Base;
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
public class ProductVariantReferenceBeanMapFactory extends ViewModelFactory<Map<String, ProductVariantReferenceBean>, ProductVariantReferenceBeanMapFactory.Data> {

    private final Locale locale;
    private final ProductDataConfig productDataConfig;
    private final ProductReverseRouter productReverseRouter;
    private final AttributeFormatter attributeFormatter;

    @Inject
    public ProductVariantReferenceBeanMapFactory(final Locale locale, final ProductDataConfig productDataConfig,
                                                 final ProductReverseRouter productReverseRouter, final AttributeFormatter attributeFormatter) {
        this.locale = locale;
        this.productDataConfig = productDataConfig;
        this.productReverseRouter = productReverseRouter;
        this.attributeFormatter = attributeFormatter;
    }

    public final Map<String, ProductVariantReferenceBean> create(final ProductProjection product, final ProductVariant variant) {
        final Data data = new Data(product, variant);
        return initializedViewModel(data);
    }

    @Override
    protected Map<String, ProductVariantReferenceBean> getViewModelInstance() {
        return new HashMap<>();
    }

    @Override
    protected final void initialize(final Map<String, ProductVariantReferenceBean> map, final Data data) {
        fillVariants(map, data);
    }

    protected void fillVariants(final Map<String, ProductVariantReferenceBean> map, final Data data) {
        data.product.getAllVariants()
                .forEach(productVariant -> {
                    final String attributeCombination = createAttributeCombination(productVariant);
                    final ProductVariantReferenceBean variantReference = createVariantReference(data);
                    map.put(attributeCombination, variantReference);
                });
    }

    private ProductVariantReferenceBean createVariantReference(final Data data) {
        final ProductVariantReferenceBean bean = new ProductVariantReferenceBean();
        bean.setId(data.variant.getId());
        bean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(locale, data.product, data.variant));
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

    protected final static class Data extends Base {

        public final ProductProjection product;
        public final ProductVariant variant;

        public Data(final ProductProjection product, final ProductVariant variant) {
            this.product = product;
            this.variant = variant;
        }
    }
}
