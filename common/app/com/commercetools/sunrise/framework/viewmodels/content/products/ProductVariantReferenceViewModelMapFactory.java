package com.commercetools.sunrise.framework.viewmodels.content.products;

import com.commercetools.sunrise.ctp.ProductAttributeSettings;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.formatters.AttributeFormatter;
import io.sphere.sdk.models.Referenceable;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.producttypes.ProductType;

import javax.inject.Inject;

import static java.util.stream.Collectors.joining;

@RequestScoped
public class ProductVariantReferenceViewModelMapFactory extends SimpleViewModelFactory<ProductVariantReferenceMapViewModel, ProductWithVariant> {

    private final ProductAttributeSettings productAttributeSettings;
    private final AttributeFormatter attributeFormatter;
    private final ProductVariantReferenceViewModelFactory productVariantReferenceViewModelFactory;

    @Inject
    public ProductVariantReferenceViewModelMapFactory(final ProductAttributeSettings productAttributeSettings, final AttributeFormatter attributeFormatter, final ProductVariantReferenceViewModelFactory productVariantReferenceViewModelFactory) {
        this.productAttributeSettings = productAttributeSettings;
        this.attributeFormatter = attributeFormatter;
        this.productVariantReferenceViewModelFactory = productVariantReferenceViewModelFactory;
    }

    protected final ProductAttributeSettings getProductAttributeSettings() {
        return productAttributeSettings;
    }

    protected final AttributeFormatter getAttributeFormatter() {
        return attributeFormatter;
    }

    protected final ProductVariantReferenceViewModelFactory getProductVariantReferenceViewModelFactory() {
        return productVariantReferenceViewModelFactory;
    }

    @Override
    protected ProductVariantReferenceMapViewModel newViewModelInstance(final ProductWithVariant productWithVariant) {
        return new ProductVariantReferenceMapViewModel();
    }

    @Override
    public final ProductVariantReferenceMapViewModel create(final ProductWithVariant productWithVariant) {
        return super.create(productWithVariant);
    }

    @Override
    protected final void initialize(final ProductVariantReferenceMapViewModel viewModel, final ProductWithVariant productWithVariant) {
        fillMap(viewModel, productWithVariant);
    }

    protected void fillMap(final ProductVariantReferenceMapViewModel map, final ProductWithVariant productWithVariant) {
        productWithVariant.getProduct().getAllVariants()
                .forEach(productVariant -> {
                    final String mapKey = createMapKey(productVariant, productWithVariant.getProduct().getProductType());
                    final ProductVariantReferenceViewModel mapValue = createMapValue(productWithVariant.getProduct(), productVariant);
                    map.put(mapKey, mapValue);
                });
    }

    private String createMapKey(final ProductVariant variant, final Referenceable<ProductType> productTypeRef) {
        return productAttributeSettings.getSelectableAttributes().stream()
                .map(variant::getAttribute)
                .map(attribute -> attribute != null ? AttributeWithProductType.of(attribute, productTypeRef) : null)
                .map(attributeFormatter::encodedValue)
                .collect(joining("-"));
    }

    private ProductVariantReferenceViewModel createMapValue(final ProductProjection product, final ProductVariant productVariant) {
        final ProductWithVariant productWithVariant = ProductWithVariant.of(product, productVariant);
        return productVariantReferenceViewModelFactory.create(productWithVariant);
    }
}
