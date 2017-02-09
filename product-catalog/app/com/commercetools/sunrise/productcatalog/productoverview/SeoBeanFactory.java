package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.models.ViewModelFactory;

import javax.inject.Singleton;

@Singleton
public class SeoBeanFactory extends ViewModelFactory<SeoBean, ProductOverviewControllerData> {

    @Override
    protected SeoBean getViewModelInstance() {
        return new SeoBean();
    }

    @Override
    public final SeoBean create(final ProductOverviewControllerData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final SeoBean model, final ProductOverviewControllerData data) {
        fillTitle(model, data);
        fillDescription(model, data);
    }

    protected void fillTitle(final SeoBean model, final ProductOverviewControllerData data) {
        if (data.getCategory() != null && data.getCategory().getMetaTitle() != null) {
            model.setTitle(data.getCategory().getMetaTitle());
        }
    }

    protected void fillDescription(final SeoBean model, final ProductOverviewControllerData data) {
        if (data.getCategory() != null && data.getCategory().getMetaDescription() != null) {
            model.setDescription(data.getCategory().getMetaDescription());
        }
    }
}
