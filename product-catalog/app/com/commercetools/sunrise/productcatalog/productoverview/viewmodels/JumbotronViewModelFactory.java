package com.commercetools.sunrise.productcatalog.productoverview.viewmodels;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.productcatalog.productoverview.ProductsWithCategory;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;

@RequestScoped
public class JumbotronViewModelFactory extends SimpleViewModelFactory<JumbotronViewModel, ProductsWithCategory> {

    private final CategoryTree categoryTree;

    @Inject
    public JumbotronViewModelFactory(final CategoryTree categoryTree) {
        this.categoryTree = categoryTree;
    }

    protected final CategoryTree getCategoryTree() {
        return categoryTree;
    }

    @Override
    protected JumbotronViewModel newViewModelInstance(final ProductsWithCategory productWithCategory) {
        return new JumbotronViewModel();
    }

    @Override
    public final JumbotronViewModel create(final ProductsWithCategory productsWithCategory) {
        return super.create(productsWithCategory);
    }

    @Override
    protected final void initialize(final JumbotronViewModel viewModel, final ProductsWithCategory productsWithCategory) {
        fillTitle(viewModel, productsWithCategory);
        fillSubtitle(viewModel, productsWithCategory);
        fillDescription(viewModel, productsWithCategory);
    }

    protected void fillTitle(final JumbotronViewModel viewModel, final ProductsWithCategory data) {
        if (data.getCategory() != null) {
            viewModel.setTitle(data.getCategory().getName());
        }
    }

    protected void fillSubtitle(final JumbotronViewModel viewModel, final ProductsWithCategory data) {
        if (data.getCategory() != null && data.getCategory().getParent() != null) {
            categoryTree.findById(data.getCategory().getParent().getId())
                    .ifPresent(parent -> viewModel.setSubtitle(parent.getName()));
        }
    }

    protected void fillDescription(final JumbotronViewModel viewModel, final ProductsWithCategory data) {
        if (data.getCategory() != null && data.getCategory().getDescription() != null) {
            viewModel.setDescription(data.getCategory().getDescription());
        }
    }
}
