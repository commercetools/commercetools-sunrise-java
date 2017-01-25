package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;

import javax.inject.Inject;
import java.util.Optional;

@RequestScoped
public class SeoBeanFactory extends ViewModelFactory<SeoBean, ProductOverviewPageData> {

    private final LocalizedStringResolver localizedStringResolver;

    @Inject
    public SeoBeanFactory(final LocalizedStringResolver localizedStringResolver) {
        this.localizedStringResolver = localizedStringResolver;
    }

    @Override
    protected SeoBean getViewModelInstance() {
        return new SeoBean();
    }

    @Override
    public final SeoBean create(final ProductOverviewPageData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final SeoBean model, final ProductOverviewPageData data) {
        fillTitle(model, data);
        fillDescription(model, data);
    }

    protected void fillTitle(final SeoBean model, final ProductOverviewPageData data) {
        if (data.category != null) {
            Optional.ofNullable(data.category.getMetaTitle())
                    .ifPresent(title -> model.setTitle(localizedStringResolver.getOrEmpty(title)));
        }
    }

    protected void fillDescription(final SeoBean model, final ProductOverviewPageData data) {
        if (data.category != null) {
            Optional.ofNullable(data.category.getMetaDescription())
                    .ifPresent(description -> model.setDescription(localizedStringResolver.getOrEmpty(description)));
        }
    }
}
