package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;

@RequestScoped
public class JumbotronBeanFactory extends ViewModelFactory<JumbotronBean, ProductOverviewControllerData> {

    private final LocalizedStringResolver localizedStringResolver;
    private final CategoryTree categoryTree;

    @Inject
    public JumbotronBeanFactory(final LocalizedStringResolver localizedStringResolver, final CategoryTree categoryTree) {
        this.localizedStringResolver = localizedStringResolver;
        this.categoryTree = categoryTree;
    }

    @Override
    protected JumbotronBean getViewModelInstance() {
        return new JumbotronBean();
    }

    @Override
    public final JumbotronBean create(final ProductOverviewControllerData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final JumbotronBean model, final ProductOverviewControllerData data) {
        fillTitle(model, data);
        fillSubtitle(model, data);
        fillDescription(model, data);
    }

    protected void fillTitle(final JumbotronBean model, final ProductOverviewControllerData data) {
        if (data.category != null) {
            model.setTitle(localizedStringResolver.getOrEmpty(data.category.getName()));
        }
    }

    protected void fillSubtitle(final JumbotronBean model, final ProductOverviewControllerData data) {
        if (data.category != null && data.category.getParent() != null) {
            categoryTree.findById(data.category.getParent().getId())
                    .ifPresent(parent -> model.setSubtitle(localizedStringResolver.getOrEmpty(parent.getName())));
        }
    }

    protected void fillDescription(final JumbotronBean model, final ProductOverviewControllerData data) {
        if (data.category != null && data.category.getDescription() != null) {
            model.setDescription(localizedStringResolver.getOrEmpty(data.category.getDescription()));
        }
    }
}
