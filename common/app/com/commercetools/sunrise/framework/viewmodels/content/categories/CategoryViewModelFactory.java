package com.commercetools.sunrise.framework.viewmodels.content.categories;

import com.commercetools.sunrise.categorytree.CategoryTreeConfiguration;
import com.commercetools.sunrise.categorytree.NavigationCategoryTree;
import com.commercetools.sunrise.categorytree.SpecialCategoryConfiguration;
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

    private final CategoryTreeConfiguration categoryTreeConfiguration;
    private final CategoryTree categoryTree;
    private final ProductReverseRouter productReverseRouter;

    @Inject
    public CategoryViewModelFactory(final CategoryTreeConfiguration categoryTreeConfiguration, @NavigationCategoryTree final CategoryTree categoryTree,
                                    final ProductReverseRouter productReverseRouter) {
        this.categoryTreeConfiguration = categoryTreeConfiguration;
        this.categoryTree = categoryTree;
        this.productReverseRouter = productReverseRouter;
    }

    protected final CategoryTreeConfiguration getCategoryTreeConfiguration() {
        return categoryTreeConfiguration;
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
        final boolean isSale = categoryTreeConfiguration.specialCategories().stream()
                .filter(SpecialCategoryConfiguration::isSale)
                .anyMatch(specialCategory -> specialCategory.externalId().equals(category.getExternalId()));
        viewModel.setSale(isSale);
    }

    protected void fillChildren(final CategoryViewModel viewModel, final Category category) {
        viewModel.setChildren(categoryTree.findChildren(category).stream()
                .map(this::create)
                .collect(toList()));
    }
}
