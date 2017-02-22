package com.commercetools.sunrise.productcatalog.productoverview.viewmodels;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.productcatalog.productoverview.ProductsWithCategory;

import javax.inject.Singleton;

@Singleton
public class SeoViewModelFactory extends ViewModelFactory<SeoViewModel, ProductsWithCategory> {

    @Override
    protected SeoViewModel getViewModelInstance() {
        return new SeoViewModel();
    }

    @Override
    public final SeoViewModel create(final ProductsWithCategory productsWithCategory) {
        return super.create(productsWithCategory);
    }

    @Override
    protected final void initialize(final SeoViewModel viewModel, final ProductsWithCategory productsWithCategory) {
        fillTitle(viewModel, productsWithCategory);
        fillDescription(viewModel, productsWithCategory);
    }

    protected void fillTitle(final SeoViewModel viewModel, final ProductsWithCategory data) {
        if (data.getCategory() != null && data.getCategory().getMetaTitle() != null) {
            viewModel.setTitle(data.getCategory().getMetaTitle());
        }
    }

    protected void fillDescription(final SeoViewModel viewModel, final ProductsWithCategory data) {
        if (data.getCategory() != null && data.getCategory().getMetaDescription() != null) {
            viewModel.setDescription(data.getCategory().getMetaDescription());
        }
    }
}
