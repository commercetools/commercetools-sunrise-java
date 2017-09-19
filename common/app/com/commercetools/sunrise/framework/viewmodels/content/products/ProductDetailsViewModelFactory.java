package com.commercetools.sunrise.framework.viewmodels.content.products;

import com.commercetools.sunrise.ctp.products.ProductAttributesSettings;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.producttypes.ProductType;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class ProductDetailsViewModelFactory extends SimpleViewModelFactory<ProductDetailsViewModel, ProductWithVariant> {

    private final ProductAttributesSettings productAttributesSettings;
    private final ProductAttributeViewModelFactory productAttributeViewModelFactory;

    @Inject
    public ProductDetailsViewModelFactory(final ProductAttributesSettings productAttributesSettings, final ProductAttributeViewModelFactory productAttributeViewModelFactory) {
        this.productAttributesSettings = productAttributesSettings;
        this.productAttributeViewModelFactory = productAttributeViewModelFactory;
    }

    protected final ProductAttributesSettings getProductAttributesSettings() {
        return productAttributesSettings;
    }

    protected final ProductAttributeViewModelFactory getProductAttributeViewModelFactory() {
        return productAttributeViewModelFactory;
    }

    @Override
    public final ProductDetailsViewModel create(final ProductWithVariant productWithVariant) {
        return super.create(productWithVariant);
    }

    @Override
    protected ProductDetailsViewModel newViewModelInstance(final ProductWithVariant productWithVariant) {
        return new ProductDetailsViewModel();
    }

    @Override
    protected final void initialize(final ProductDetailsViewModel viewModel, final ProductWithVariant productWithVariant) {
        fillList(viewModel, productWithVariant);
    }

    protected void fillList(final ProductDetailsViewModel viewModel, final ProductWithVariant productWithVariant) {
        final List<ProductAttributeViewModel> attributes = productAttributesSettings.displayed().stream()
                .map(productWithVariant.getVariant()::getAttribute)
                .filter(Objects::nonNull)
                .map(attribute -> createProductAttributeViewModel(productWithVariant, attribute))
                .collect(toList());
        viewModel.setFeatures(attributes);
    }

    private ProductAttributeViewModel createProductAttributeViewModel(final ProductWithVariant productWithVariant, final Attribute attribute) {
        final Reference<ProductType> productTypeRef = productWithVariant.getProduct().getProductType();
        return productAttributeViewModelFactory.create(AttributeWithProductType.of(attribute, productTypeRef));
    }
}
