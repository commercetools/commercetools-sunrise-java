package com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.categorytree.viewmodels;

import com.commercetools.sunrise.categorytree.NavigationCategoryTree;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.localization.UserLanguage;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.AbstractFacetOptionViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.FacetOptionViewModel;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.TermStats;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

import static com.commercetools.sunrise.framework.viewmodels.forms.QueryStringUtils.buildUri;
import static com.commercetools.sunrise.framework.viewmodels.forms.QueryStringUtils.extractQueryString;

@RequestScoped
public class CategoryTreeFacetOptionViewModelFactory extends AbstractFacetOptionViewModelFactory<TermFacetResult, Category, Category> {

    private final List<Locale> locales;
    private final CategoryTree categoryTree;
    private final ProductReverseRouter productReverseRouter;
    private static final Set<String> IGNORED_PARAMS = Collections.singleton("page");

    @Inject
    public CategoryTreeFacetOptionViewModelFactory(final UserLanguage userLanguage, @NavigationCategoryTree final CategoryTree categoryTree,
                                                   final ProductReverseRouter productReverseRouter) {
        this.locales = userLanguage.locales();
        this.categoryTree = categoryTree;
        this.productReverseRouter = productReverseRouter;
    }

    protected final List<Locale> getLocales() {
        return locales;
    }

    protected final CategoryTree getCategoryTree() {
        return categoryTree;
    }

    protected final ProductReverseRouter getProductReverseRouter() {
        return productReverseRouter;
    }

    @Override
    public final FacetOptionViewModel create(final TermFacetResult stats, final Category value, @Nullable final Category selectedValue) {
        return super.create(stats, value, selectedValue);
    }

    @Override
    protected final void initialize(final FacetOptionViewModel viewModel, final TermFacetResult stats, final Category value, @Nullable final Category selectedValue) {
        super.initialize(viewModel, stats, value, selectedValue);
        fillChildren(viewModel, stats, value, selectedValue);
    }

    @Override
    protected void fillLabel(final FacetOptionViewModel viewModel, final TermFacetResult stats, final Category category, @Nullable final Category selectedValue) {
        viewModel.setLabel(category.getName().find(locales).orElseGet(category::getId));
    }

    @Override
    protected void fillValue(final FacetOptionViewModel viewModel, final TermFacetResult stats, final Category category, @Nullable final Category selectedValue) {
        productReverseRouter.productOverviewPageCall(category).ifPresent(call -> {
            viewModel.setValue(buildUri(call.url(), extractQueryString(Http.Context.current().request(), IGNORED_PARAMS)));
        });
    }

    @Override
    protected void fillSelected(final FacetOptionViewModel viewModel, final TermFacetResult stats, final Category category, @Nullable final Category selectedValue) {
        if (selectedValue != null) {
            viewModel.setSelected(category.getId().equals(selectedValue.getId()));
        }
    }

    @Override
    protected void fillCount(final FacetOptionViewModel viewModel, final TermFacetResult stats, final Category category, @Nullable final Category selectedValue) {
        final Long count = findTermStats(stats, category)
                .map(TermStats::getProductCount)
                .filter(Objects::nonNull)
                .orElse(0L);
        viewModel.setCount(count);
    }

    protected void fillChildren(final FacetOptionViewModel viewModel, final TermFacetResult stats, final Category category, @Nullable final Category selectedValue) {
        final List<FacetOptionViewModel> childrenViewModels = new ArrayList<>();
        categoryTree.findChildren(category).forEach(child -> {
            final FacetOptionViewModel childViewModel = create(stats, child, selectedValue);
            if (childViewModel.getCount() > 0) {
                childrenViewModels.add(childViewModel);
                viewModel.setCount(viewModel.getCount() + childViewModel.getCount());
                viewModel.setSelected(viewModel.isSelected() || childViewModel.isSelected());
            }
        });
        if (viewModel.isSelected()) {
            viewModel.setChildren(childrenViewModels);
        }
    }

    private Optional<TermStats> findTermStats(final TermFacetResult termFacetResult, final Category category) {
        return termFacetResult.getTerms().stream()
                .filter(stats -> stats.getTerm().equals(category.getId()))
                .findAny();
    }
}
