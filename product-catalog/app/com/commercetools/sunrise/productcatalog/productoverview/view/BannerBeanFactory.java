package com.commercetools.sunrise.productcatalog.productoverview.view;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.productcatalog.productoverview.ProductsWithCategory;

import javax.inject.Singleton;

@Singleton
public class BannerBeanFactory extends ViewModelFactory<BannerBean, ProductsWithCategory> {

    @Override
    protected BannerBean getViewModelInstance() {
        return new BannerBean();
    }

    @Override
    public final BannerBean create(final ProductsWithCategory data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final BannerBean model, final ProductsWithCategory data) {
        fillTitle(model, data);
        fillDescription(model, data);
    }

    protected void fillDescription(final BannerBean bean, final ProductsWithCategory data) {
        if (data.getCategory() != null && data.getCategory().getDescription() != null) {
            bean.setDescription(data.getCategory().getDescription());
        }
    }

    protected void fillTitle(final BannerBean bean, final ProductsWithCategory data) {
        if (data.getCategory() != null) {
            bean.setTitle(data.getCategory().getName());
        }
    }
}
