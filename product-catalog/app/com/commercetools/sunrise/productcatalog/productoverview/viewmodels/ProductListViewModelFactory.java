package com.commercetools.sunrise.productcatalog.productoverview.viewmodels;

import com.commercetools.sunrise.core.injection.RequestScoped;
import com.commercetools.sunrise.core.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.models.products.ProductWithVariant;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;
import java.util.List;

@RequestScoped
public class ProductListViewModelFactory extends SimpleViewModelFactory<ProductListViewModel, List<ProductProjection>> {

    @Inject
    public ProductListViewModelFactory() {
    }


    @Override
    protected ProductListViewModel newViewModelInstance(final List<ProductProjection> products) {
        return new ProductListViewModel();
    }

    @Override
    public final ProductListViewModel create(final List<ProductProjection> products) {
        return super.create(products);
    }

    @Override
    protected final void initialize(final ProductListViewModel viewModel, final List<ProductProjection> products) {
        fillList(viewModel, products);
    }

    protected void fillList(final ProductListViewModel viewModel, final List<ProductProjection> products) {
        viewModel.setList(products);
    }

    private ProductWithVariant createProductWithVariant(final ProductProjection product) {
        final ProductVariant selectedVariant = product.findFirstMatchingVariant()
                .orElseGet(product::getMasterVariant);
        return ProductWithVariant.of(product, selectedVariant);
    }
}
