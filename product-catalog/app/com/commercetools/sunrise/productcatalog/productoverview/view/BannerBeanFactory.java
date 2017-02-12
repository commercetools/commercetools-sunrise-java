package com.commercetools.sunrise.productcatalog.productoverview.view;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.productcatalog.productoverview.ProductOverviewControllerData;

import javax.inject.Singleton;

@Singleton
public class BannerBeanFactory extends ViewModelFactory<BannerBean, ProductOverviewControllerData> {

    @Override
    protected BannerBean getViewModelInstance() {
        return new BannerBean();
    }

    @Override
    public final BannerBean create(final ProductOverviewControllerData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final BannerBean model, final ProductOverviewControllerData data) {
        fillTitle(model, data);
        fillDescription(model, data);
    }

    protected void fillDescription(final BannerBean bean, final ProductOverviewControllerData data) {
        if (data.getCategory() != null && data.getCategory().getDescription() != null) {
            bean.setDescription(data.getCategory().getDescription());
        }
    }

    protected void fillTitle(final BannerBean bean, final ProductOverviewControllerData data) {
        if (data.getCategory() != null) {
            bean.setTitle(data.getCategory().getName());
        }
    }
}
