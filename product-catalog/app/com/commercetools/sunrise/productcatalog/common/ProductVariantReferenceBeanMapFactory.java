package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.models.ProductWithVariant;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.utils.AttributeFormatter;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.Attribute;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

@RequestScoped
public class ProductVariantReferenceBeanMapFactory extends ViewModelFactory<Map<String, ProductVariantReferenceBean>, ProductWithVariant> {

    private final ProductDataConfig productDataConfig;
    private final AttributeFormatter attributeFormatter;
    private final ProductVariantReferenceBeanFactory productVariantReferenceBeanFactory;

    @Inject
    public ProductVariantReferenceBeanMapFactory(final ProductDataConfig productDataConfig, final AttributeFormatter attributeFormatter, final ProductVariantReferenceBeanFactory productVariantReferenceBeanFactory) {
        this.productDataConfig = productDataConfig;
        this.attributeFormatter = attributeFormatter;
        this.productVariantReferenceBeanFactory = productVariantReferenceBeanFactory;
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
        fillMap(model, productWithVariant);
    }

    protected void fillMap(final Map<String, ProductVariantReferenceBean> map, final ProductWithVariant productWithVariant) {
        productWithVariant.getProduct().getAllVariants()
                .forEach(productVariant -> map.put(createMapKey(productVariant), createMapValue(productWithVariant)));
    }

    private String createMapKey(final ProductVariant variant) {
        return productDataConfig.getSelectableAttributes().stream()
                .map(selectableAttr -> createAttributeKey(variant.getAttribute(selectableAttr)))
                .collect(joining("-"));
    }

    private ProductVariantReferenceBean createMapValue(final ProductWithVariant productWithVariant) {
        return productVariantReferenceBeanFactory.create(productWithVariant);
    }

    private String createAttributeKey(@Nullable final Attribute nullableAttribute) {
        return Optional.ofNullable(nullableAttribute)
                .map(attributeFormatter::valueAsKey)
                .orElse("");
    }
}
