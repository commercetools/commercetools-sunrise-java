package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.models.ProductWithVariant;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.utils.AttributeFormatter;
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
public class ProductVariantReferenceBeanMapFactory extends ViewModelFactory<Map<String, ProductVariantReferenceBean>, ProductWithVariant> {

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

    @Override
    public final Map<String, ProductVariantReferenceBean> create(final ProductWithVariant data) {
        return super.create(data);
    }

    @Override
    protected Map<String, ProductVariantReferenceBean> getViewModelInstance() {
        return new HashMap<>();
    }

    @Override
    protected final void initialize(final Map<String, ProductVariantReferenceBean> model, final ProductWithVariant productWithVariant) {
        fillVariants(model, productWithVariant);
    }

    protected void fillVariants(final Map<String, ProductVariantReferenceBean> map, final ProductWithVariant productWithVariant) {
        productWithVariant.getProduct().getAllVariants()
                .forEach(productVariant -> {
                    final String attributeCombination = createAttributeCombination(productVariant);
                    final ProductVariantReferenceBean variantReference = createVariantReference(productWithVariant);
                    map.put(attributeCombination, variantReference);
                });
    }

    private ProductVariantReferenceBean createVariantReference(final ProductWithVariant productWithVariant) {
        final ProductVariantReferenceBean bean = new ProductVariantReferenceBean();
        bean.setId(productWithVariant.getVariant().getId());
        bean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(locale, productWithVariant.getProduct(), productWithVariant.getVariant()));
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
