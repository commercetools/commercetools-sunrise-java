package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.common.ctp.ProductAttributeSettings;
import com.commercetools.sunrise.common.models.AttributeWithProductType;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.utils.AttributeFormatter;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.productcatalog.productdetail.ProductWithVariant;
import io.sphere.sdk.models.Referenceable;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.producttypes.ProductType;

import javax.inject.Inject;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

@RequestScoped
public class ProductVariantReferenceViewModelMapFactory extends ViewModelFactory<ProductVariantReferenceMapViewModel, ProductWithVariant> {

    private final ProductAttributeSettings productAttributeSettings;
    private final AttributeFormatter attributeFormatter;
    private final ProductVariantReferenceViewModelFactory productVariantReferenceViewModelFactory;

    @Inject
    public ProductVariantReferenceViewModelMapFactory(final ProductAttributeSettings productAttributeSettings, final AttributeFormatter attributeFormatter, final ProductVariantReferenceViewModelFactory productVariantReferenceViewModelFactory) {
        this.productAttributeSettings = productAttributeSettings;
        this.attributeFormatter = attributeFormatter;
        this.productVariantReferenceViewModelFactory = productVariantReferenceViewModelFactory;
    }

    @Override
    protected ProductVariantReferenceMapViewModel getViewModelInstance() {
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
                .filter(Objects::nonNull)
                .map(attribute -> attributeFormatter.valueAsKey(AttributeWithProductType.of(attribute, productTypeRef)))
                .collect(joining("-"));
    }

    private ProductVariantReferenceViewModel createMapValue(final ProductProjection product, final ProductVariant productVariant) {
        final ProductWithVariant productWithVariant = ProductWithVariant.of(product, productVariant);
        return productVariantReferenceViewModelFactory.create(productWithVariant);
    }
}
