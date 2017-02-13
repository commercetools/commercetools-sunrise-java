package com.commercetools.sunrise.productcatalog.productoverview.view;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.productcatalog.productoverview.ProductsWithCategory;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;

@RequestScoped
public class JumbotronBeanFactory extends ViewModelFactory<JumbotronBean, ProductsWithCategory> {

    private final CategoryTree categoryTree;

    @Inject
    public JumbotronBeanFactory(final CategoryTree categoryTree) {
        this.categoryTree = categoryTree;
    }

    @Override
    protected JumbotronBean getViewModelInstance() {
        return new JumbotronBean();
    }

    @Override
    public final JumbotronBean create(final ProductsWithCategory data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final JumbotronBean model, final ProductsWithCategory data) {
        fillTitle(model, data);
        fillSubtitle(model, data);
        fillDescription(model, data);
    }

    protected void fillTitle(final JumbotronBean model, final ProductsWithCategory data) {
        if (data.getCategory() != null) {
            model.setTitle(data.getCategory().getName());
        }
    }

    protected void fillSubtitle(final JumbotronBean model, final ProductsWithCategory data) {
        if (data.getCategory() != null && data.getCategory().getParent() != null) {
            categoryTree.findById(data.getCategory().getParent().getId())
                    .ifPresent(parent -> model.setSubtitle(parent.getName()));
        }
    }

    protected void fillDescription(final JumbotronBean model, final ProductsWithCategory data) {
        if (data.getCategory() != null && data.getCategory().getDescription() != null) {
            model.setDescription(data.getCategory().getDescription());
        }
    }
}
