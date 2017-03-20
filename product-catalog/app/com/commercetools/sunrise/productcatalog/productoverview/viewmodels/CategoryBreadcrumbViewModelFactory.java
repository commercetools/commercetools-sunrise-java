package com.commercetools.sunrise.productcatalog.productoverview.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.breadcrumbs.BreadcrumbViewModel;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.framework.viewmodels.content.breadcrumbs.AbstractBreadcrumbViewModelFactory;
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
    public final BreadcrumbViewModel create(final ProductsWithCategory productsWithCategory) {
        return super.create(productsWithCategory);
    }

    @Override
    protected BreadcrumbViewModel newViewModelInstance(final ProductsWithCategory productsWithCategory) {
        return new BreadcrumbViewModel();
    }

    @Override
    protected final void initialize(final BreadcrumbViewModel viewModel, final ProductsWithCategory productsWithCategory) {
        super.initialize(viewModel, productsWithCategory);
    }

    @Override
    protected void fillLinks(final BreadcrumbViewModel viewModel, final ProductsWithCategory productsWithCategory) {
        if (productsWithCategory.getCategory() != null) {
            viewModel.setLinks(createCategoryTreeLinks(productsWithCategory.getCategory()));
        }
    }
}
