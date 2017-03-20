package com.commercetools.sunrise.productcatalog.productoverview.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.productcatalog.productoverview.ProductsWithCategory;

import javax.inject.Singleton;

@Singleton
public class BannerViewModelFactory extends SimpleViewModelFactory<BannerViewModel, ProductsWithCategory> {

    @Override
    protected BannerViewModel newViewModelInstance(final ProductsWithCategory productsWithCategory) {
        return new BannerViewModel();
    }

    @Override
    public final BannerViewModel create(final ProductsWithCategory productsWithCategory) {
        return super.create(productsWithCategory);
    }

    @Override
    protected final void initialize(final BannerViewModel viewModel, final ProductsWithCategory productsWithCategory) {
        fillTitle(viewModel, productsWithCategory);
        fillDescription(viewModel, productsWithCategory);
    }

    protected void fillDescription(final BannerViewModel viewModel, final ProductsWithCategory data) {
        if (data.getCategory() != null && data.getCategory().getDescription() != null) {
            viewModel.setDescription(data.getCategory().getDescription());
        }
    }

    protected void fillTitle(final BannerViewModel viewModel, final ProductsWithCategory data) {
        if (data.getCategory() != null) {
            viewModel.setTitle(data.getCategory().getName());
        }
    }
}
