package com.commercetools.sunrise.framework.viewmodels.content.products;

import com.commercetools.sunrise.ctp.categories.NewCategoryTree;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;

import static com.commercetools.sunrise.framework.viewmodels.content.products.ProductPriceUtils.hasDiscount;

@RequestScoped
public class ProductThumbnailViewModelFactory extends SimpleViewModelFactory<ProductThumbnailViewModel, ProductWithVariant> {

    private final CategoryTree categoryTreeInNew;
    private final ProductViewModelFactory productViewModelFactory;

    @Inject
    public ProductThumbnailViewModelFactory(@NewCategoryTree final CategoryTree categoryTreeInNew, final ProductViewModelFactory productViewModelFactory) {
        this.categoryTreeInNew = categoryTreeInNew;
        this.productViewModelFactory = productViewModelFactory;
    }

    protected final CategoryTree getCategoryTreeInNew() {
        return categoryTreeInNew;
    }

    protected final ProductViewModelFactory getProductViewModelFactory() {
        return productViewModelFactory;
    }

    @Override
    protected ProductThumbnailViewModel newViewModelInstance(final ProductWithVariant productWithVariant) {
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

    protected void fillProduct(final ProductThumbnailViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setProduct(productViewModelFactory.create(productWithVariant));
    }

    protected void fillNew(final ProductThumbnailViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setNew(productWithVariant.getProduct().getCategories().stream()
                .anyMatch(category -> categoryTreeInNew.findById(category.getId()).isPresent()));
    }

    protected void fillSale(final ProductThumbnailViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setSale(hasDiscount(productWithVariant.getVariant()));
    }
}
