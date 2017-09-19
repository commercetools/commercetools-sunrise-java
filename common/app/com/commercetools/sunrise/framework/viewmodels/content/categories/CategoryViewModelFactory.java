package com.commercetools.sunrise.framework.viewmodels.content.categories;

import com.commercetools.sunrise.ctp.categories.CategoriesSettings;
import com.commercetools.sunrise.ctp.categories.NavigationCategoryTree;
import com.commercetools.sunrise.ctp.categories.SpecialCategorySettings;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import play.mvc.Call;

import javax.inject.Inject;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class CategoryViewModelFactory extends SimpleViewModelFactory<CategoryViewModel, Category> {

    private final CategoriesSettings categoriesSettings;
    private final CategoryTree categoryTree;
    private final ProductReverseRouter productReverseRouter;

    @Inject
    public CategoryViewModelFactory(final CategoriesSettings categoriesSettings, @NavigationCategoryTree final CategoryTree categoryTree,
                                    final ProductReverseRouter productReverseRouter) {
        this.categoriesSettings = categoriesSettings;
        this.categoryTree = categoryTree;
        this.productReverseRouter = productReverseRouter;
    }

    protected final CategoriesSettings getCategoriesSettings() {
        return categoriesSettings;
    }

    protected final CategoryTree getCategoryTree() {
        return categoryTree;
    }

    protected final ProductReverseRouter getProductReverseRouter() {
        return productReverseRouter;
    }

    @Override
    protected CategoryViewModel newViewModelInstance(final Category category) {
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
                .productOverviewPageCall(category)
                .map(Call::url)
                .orElse(""));
    }

    protected void fillSale(final CategoryViewModel viewModel, final Category category) {
        final boolean isSale = categoriesSettings.specialCategories().stream()
                .filter(SpecialCategorySettings::isSale)
                .anyMatch(specialCategory -> specialCategory.externalId().equals(category.getExternalId()));
        viewModel.setSale(isSale);
    }

    protected void fillChildren(final CategoryViewModel viewModel, final Category category) {
        viewModel.setChildren(categoryTree.findChildren(category).stream()
                .map(this::create)
                .collect(toList()));
    }
}