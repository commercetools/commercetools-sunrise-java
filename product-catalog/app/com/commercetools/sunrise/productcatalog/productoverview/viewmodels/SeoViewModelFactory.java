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
    public final SeoViewModel create(final ProductsWithCategory data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final SeoViewModel model, final ProductsWithCategory data) {
        fillTitle(model, data);
        fillDescription(model, data);
    }

    protected void fillTitle(final SeoViewModel model, final ProductsWithCategory data) {
        if (data.getCategory() != null && data.getCategory().getMetaTitle() != null) {
            model.setTitle(data.getCategory().getMetaTitle());
        }
    }

    protected void fillDescription(final SeoViewModel model, final ProductsWithCategory data) {
        if (data.getCategory() != null && data.getCategory().getMetaDescription() != null) {
            model.setDescription(data.getCategory().getMetaDescription());
        }
    }
}
