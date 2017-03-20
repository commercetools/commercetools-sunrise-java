package com.commercetools.sunrise.productcatalog.productoverview.viewmodels;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.productcatalog.productdetail.ProductWithVariant;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class ProductListViewModelFactory extends SimpleViewModelFactory<ProductListViewModel, Iterable<ProductProjection>> {

    private final ProductThumbnailViewModelFactory productThumbnailViewModelFactory;

    @Inject
    public ProductListViewModelFactory(final ProductThumbnailViewModelFactory productThumbnailViewModelFactory) {
        this.productThumbnailViewModelFactory = productThumbnailViewModelFactory;
    }

    protected final ProductThumbnailViewModelFactory getProductThumbnailViewModelFactory() {
        return productThumbnailViewModelFactory;
    }

    @Override
    protected ProductListViewModel newViewModelInstance(final Iterable<ProductProjection> products) {
        return new ProductListViewModel();
    }

    @Override
    public final ProductListViewModel create(final Iterable<ProductProjection> products) {
        return super.create(products);
    }

    @Override
    protected final void initialize(final ProductListViewModel viewModel, final Iterable<ProductProjection> products) {
        fillList(viewModel, products);
    }

    protected void fillList(final ProductListViewModel viewModel, final Iterable<ProductProjection> products) {
        final List<ProductThumbnailViewModel> list = new ArrayList<>();
        products.forEach(product -> list.add(productThumbnailViewModelFactory.create(createProductWithVariant(product))));
        viewModel.setList(list);
    }

    private ProductWithVariant createProductWithVariant(final ProductProjection product) {
        final ProductVariant selectedVariant = product.findFirstMatchingVariant()
                .orElseGet(product::getMasterVariant);
        return ProductWithVariant.of(product, selectedVariant);
    }
}
