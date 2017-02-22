package com.commercetools.sunrise.productcatalog.productoverview.viewmodels;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.productcatalog.productoverview.ProductsWithCategory;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;

@RequestScoped
public class JumbotronViewModelFactory extends ViewModelFactory<JumbotronViewModel, ProductsWithCategory> {

    private final CategoryTree categoryTree;

    @Inject
    public JumbotronViewModelFactory(final CategoryTree categoryTree) {
        this.categoryTree = categoryTree;
    }

    @Override
    protected JumbotronViewModel getViewModelInstance() {
        return new JumbotronViewModel();
    }

    @Override
    public final JumbotronViewModel create(final ProductsWithCategory data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final JumbotronViewModel model, final ProductsWithCategory data) {
        fillTitle(model, data);
        fillSubtitle(model, data);
        fillDescription(model, data);
    }

    protected void fillTitle(final JumbotronViewModel model, final ProductsWithCategory data) {
        if (data.getCategory() != null) {
            model.setTitle(data.getCategory().getName());
        }
    }

    protected void fillSubtitle(final JumbotronViewModel model, final ProductsWithCategory data) {
        if (data.getCategory() != null && data.getCategory().getParent() != null) {
            categoryTree.findById(data.getCategory().getParent().getId())
                    .ifPresent(parent -> model.setSubtitle(parent.getName()));
        }
    }

    protected void fillDescription(final JumbotronViewModel model, final ProductsWithCategory data) {
        if (data.getCategory() != null && data.getCategory().getDescription() != null) {
            model.setDescription(data.getCategory().getDescription());
        }
    }
}
