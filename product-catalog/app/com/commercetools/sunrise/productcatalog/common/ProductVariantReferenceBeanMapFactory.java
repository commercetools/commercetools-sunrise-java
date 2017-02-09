package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.ctp.AttributeSettings;
import com.commercetools.sunrise.common.models.AttributeWithProductType;
import com.commercetools.sunrise.common.models.ProductWithVariant;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.utils.AttributeFormatter;
import io.sphere.sdk.models.Referenceable;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.producttypes.ProductType;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

@RequestScoped
public class ProductVariantReferenceBeanMapFactory extends ViewModelFactory<Map<String, ProductVariantReferenceBean>, ProductWithVariant> {

    private final AttributeSettings attributeSettings;
    private final AttributeFormatter attributeFormatter;
    private final ProductVariantReferenceBeanFactory productVariantReferenceBeanFactory;

    @Inject
    public ProductVariantReferenceBeanMapFactory(final AttributeSettings attributeSettings, final AttributeFormatter attributeFormatter, final ProductVariantReferenceBeanFactory productVariantReferenceBeanFactory) {
        this.attributeSettings = attributeSettings;
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
                .forEach(productVariant -> {
                    final String mapKey = createMapKey(productVariant, productWithVariant.getProduct().getProductType());
                    final ProductVariantReferenceBean mapValue = createMapValue(productWithVariant.getProduct(), productVariant);
                    map.put(mapKey, mapValue);
                });
    }

    private String createMapKey(final ProductVariant variant, final Referenceable<ProductType> productTypeRef) {
        return attributeSettings.getSelectableAttributes().stream()
                .map(variant::getAttribute)
                .filter(Objects::nonNull)
                .map(attribute -> attributeFormatter.valueAsKey(new AttributeWithProductType(attribute, productTypeRef)))
                .collect(joining("-"));
    }

    private ProductVariantReferenceBean createMapValue(final ProductProjection product, final ProductVariant productVariant) {
        final ProductWithVariant productWithVariant = new ProductWithVariant(product, productVariant);
        return productVariantReferenceBeanFactory.create(productWithVariant);
    }
}
