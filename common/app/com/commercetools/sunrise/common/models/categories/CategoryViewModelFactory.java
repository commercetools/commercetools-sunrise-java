package com.commercetools.sunrise.common.models.categories;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.ProductReverseRouter;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import play.Configuration;
import play.mvc.Call;

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
    protected CategoryViewModel getViewModelInstance(final Category category) {
        return new CategoryViewModel();
    }

    @Override
    public final CategoryViewModel create(final Category category) {
        return super.create(category);
    }

    @Override
    protected final void initialize(final CategoryViewModel viewModel, final Category category) {
        fillText(viewModel, category);
        fillUrl(viewModel, category);
        fillSale(viewModel, category);
        fillChildren(viewModel, category);
    }

    protected void fillText(final CategoryViewModel viewModel, final Category category) {
        viewModel.setText(category.getName());
    }

    protected void fillUrl(final CategoryViewModel viewModel, final Category category) {
        viewModel.setUrl(productReverseRouter
                .productOverviewPageCallByCategorySlug(category)
                .map(Call::url)
                .orElse(""));
    }

    protected void fillSale(final CategoryViewModel viewModel, final Category category) {
        viewModel.setSale(Optional.ofNullable(category.getExternalId())
                .map(id -> id.equals(saleCategoryExtId))
                .orElse(false));
    }

    protected void fillChildren(final CategoryViewModel viewModel, final Category category) {
        viewModel.setChildren(categoryTree.findChildren(category).stream()
                .map(this::create)
                .collect(toList()));
    }
}
