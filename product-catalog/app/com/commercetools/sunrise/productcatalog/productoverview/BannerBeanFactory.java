package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;

import javax.inject.Inject;
import java.util.Optional;

@RequestScoped
public class BannerBeanFactory extends ViewModelFactory<BannerBean, ProductOverviewPageData> {

    private final LocalizedStringResolver localizedStringResolver;

    @Inject
    public BannerBeanFactory(final LocalizedStringResolver localizedStringResolver) {
        this.localizedStringResolver = localizedStringResolver;
    }

    @Override
    protected BannerBean getViewModelInstance() {
        return new BannerBean();
    }

    @Override
    public final BannerBean create(final ProductOverviewPageData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final BannerBean model, final ProductOverviewPageData data) {
        fillTitle(model, data);
        fillDescription(model, data);
    }

    protected void fillDescription(final BannerBean bean, final ProductOverviewPageData data) {
        if (data.category != null) {
            Optional.ofNullable(data.category.getDescription())
                    .ifPresent(description -> bean.setDescription(localizedStringResolver.getOrEmpty(description)));
        }
    }

    protected void fillTitle(final BannerBean bean, final ProductOverviewPageData data) {
        if (data.category != null) {
            bean.setTitle(localizedStringResolver.getOrEmpty(data.category.getName()));
        }
    }
}
