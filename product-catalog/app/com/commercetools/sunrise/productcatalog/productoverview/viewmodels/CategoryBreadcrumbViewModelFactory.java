package com.commercetools.sunrise.productcatalog.productoverview.viewmodels;

import com.commercetools.sunrise.common.models.BreadcrumbViewModel;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.ProductReverseRouter;
import com.commercetools.sunrise.common.models.AbstractBreadcrumbViewModelFactory;
import com.commercetools.sunrise.productcatalog.productoverview.ProductsWithCategory;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;

@RequestScoped
public class CategoryBreadcrumbViewModelFactory extends AbstractBreadcrumbViewModelFactory<ProductsWithCategory> {

    @Inject
    public CategoryBreadcrumbViewModelFactory(final CategoryTree categoryTree, final ProductReverseRouter productReverseRouter) {
        super(categoryTree, productReverseRouter);
    }

    @Override
    public final BreadcrumbViewModel create(final ProductsWithCategory data) {
        return super.create(data);
    }

    @Override
    protected BreadcrumbViewModel getViewModelInstance() {
        return new BreadcrumbViewModel();
    }

    @Override
    protected final void initialize(final BreadcrumbViewModel model, final ProductsWithCategory data) {
        super.initialize(model, data);
    }

    @Override
    protected void fillLinks(final BreadcrumbViewModel model, final ProductsWithCategory data) {
        if (data.getCategory() != null) {
            model.setLinks(createCategoryTreeLinks(data.getCategory()));
        }
    }
}
