package com.commercetools.sunrise.productcatalog.productoverview.view;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.productcatalog.productoverview.ProductsWithCategory;

import javax.inject.Singleton;

@Singleton
public class SeoBeanFactory extends ViewModelFactory<SeoBean, ProductsWithCategory> {

    @Override
    protected SeoBean getViewModelInstance() {
        return new SeoBean();
    }

    @Override
    public final SeoBean create(final ProductsWithCategory data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final SeoBean model, final ProductsWithCategory data) {
        fillTitle(model, data);
        fillDescription(model, data);
    }

    protected void fillTitle(final SeoBean model, final ProductsWithCategory data) {
        if (data.getCategory() != null && data.getCategory().getMetaTitle() != null) {
            model.setTitle(data.getCategory().getMetaTitle());
        }
    }

    protected void fillDescription(final SeoBean model, final ProductsWithCategory data) {
        if (data.getCategory() != null && data.getCategory().getMetaDescription() != null) {
            model.setDescription(data.getCategory().getMetaDescription());
        }
    }
}
