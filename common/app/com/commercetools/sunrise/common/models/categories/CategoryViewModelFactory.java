package com.commercetools.sunrise.common.models.categories;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.ProductReverseRouter;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class CategoryViewModelFactory extends ViewModelFactory<CategoryViewModel, Category> {

    @Nullable
    private final String saleCategoryExtId;
    private final CategoryTree categoryTree;
    private final ProductReverseRouter productReverseRouter;

    @Inject
    public CategoryViewModelFactory(final Configuration configuration, final CategoryTree categoryTree,
                                    final ProductReverseRouter productReverseRouter) {
        this.saleCategoryExtId = configuration.getString("common.saleCategoryExternalId");
        this.categoryTree = categoryTree;
        this.productReverseRouter = productReverseRouter;
    }

    @Override
    protected CategoryViewModel getViewModelInstance() {
        return new CategoryViewModel();
    }

    @Override
    public final CategoryViewModel create(final Category data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final CategoryViewModel model, final Category data) {
        fillText(model, data);
        fillUrl(model, data);
        fillSale(model, data);
        fillChildren(model, data);
    }

    protected void fillText(final CategoryViewModel model, final Category category) {
        model.setText(category.getName());
    }

    protected void fillUrl(final CategoryViewModel model, final Category category) {
        model.setUrl(productReverseRouter.productOverviewPageUrlOrEmpty(category));
    }

    protected void fillSale(final CategoryViewModel model, final Category category) {
        model.setSale(Optional.ofNullable(category.getExternalId())
                .map(id -> id.equals(saleCategoryExtId))
                .orElse(false));
    }

    protected void fillChildren(final CategoryViewModel model, final Category category) {
        model.setChildren(categoryTree.findChildren(category).stream()
                .map(this::create)
                .collect(toList()));
    }
}
