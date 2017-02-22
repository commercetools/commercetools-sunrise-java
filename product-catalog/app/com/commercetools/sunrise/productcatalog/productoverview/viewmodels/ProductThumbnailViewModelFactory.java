package com.commercetools.sunrise.productcatalog.productoverview.viewmodels;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.productcatalog.productdetail.viewmodels.ProductViewModelFactory;
import com.commercetools.sunrise.productcatalog.productdetail.ProductWithVariant;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;
import javax.inject.Named;

import static com.commercetools.sunrise.common.utils.ProductPriceUtils.hasDiscount;

@RequestScoped
public class ProductThumbnailViewModelFactory extends ViewModelFactory<ProductThumbnailViewModel, ProductWithVariant> {

    private final CategoryTree categoryTreeInNew;
    private final ProductViewModelFactory productViewModelFactory;

    @Inject
    public ProductThumbnailViewModelFactory(@Named("new") final CategoryTree categoryTreeInNew, final ProductViewModelFactory productViewModelFactory) {
        this.categoryTreeInNew = categoryTreeInNew;
        this.productViewModelFactory = productViewModelFactory;
    }

    @Override
    protected ProductThumbnailViewModel getViewModelInstance() {
        return new ProductThumbnailViewModel();
    }

    @Override
    public final ProductThumbnailViewModel create(final ProductWithVariant productWithVariant) {
        return super.create(productWithVariant);
    }

    @Override
    protected final void initialize(final ProductThumbnailViewModel viewModel, final ProductWithVariant productWithVariant) {
        fillProduct(viewModel, productWithVariant);
        fillNew(viewModel, productWithVariant);
        fillSale(viewModel, productWithVariant);
    }

    protected void fillProduct(final ProductThumbnailViewModel model, final ProductWithVariant productWithVariant) {
        model.setProduct(productViewModelFactory.create(productWithVariant));
    }

    protected void fillNew(final ProductThumbnailViewModel model, final ProductWithVariant productWithVariant) {
        model.setNew(productWithVariant.getProduct().getCategories().stream()
                .anyMatch(category -> categoryTreeInNew.findById(category.getId()).isPresent()));
    }

    protected void fillSale(final ProductThumbnailViewModel model, final ProductWithVariant productWithVariant) {
        model.setSale(hasDiscount(productWithVariant.getVariant()));
    }
}
