package com.commercetools.sunrise.productcatalog.productoverview.viewmodels;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.productcatalog.productoverview.ProductsWithCategory;

import javax.inject.Singleton;

@Singleton
public class BannerViewModelFactory extends ViewModelFactory<BannerViewModel, ProductsWithCategory> {

    @Override
    protected BannerViewModel getViewModelInstance() {
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

    protected void fillDescription(final BannerViewModel model, final ProductsWithCategory data) {
        if (data.getCategory() != null && data.getCategory().getDescription() != null) {
            model.setDescription(data.getCategory().getDescription());
        }
    }

    protected void fillTitle(final BannerViewModel model, final ProductsWithCategory data) {
        if (data.getCategory() != null) {
            model.setTitle(data.getCategory().getName());
        }
    }
}
