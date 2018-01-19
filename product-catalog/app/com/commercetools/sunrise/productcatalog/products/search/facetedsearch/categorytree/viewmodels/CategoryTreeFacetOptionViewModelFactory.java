package com.commercetools.sunrise.productcatalog.products.search.facetedsearch.categorytree.viewmodels;

import com.commercetools.sunrise.core.i18n.I18nResolver;
import com.commercetools.sunrise.core.injection.RequestScoped;
import com.commercetools.sunrise.models.categories.CachedCategoryTree;
import com.commercetools.sunrise.models.search.facetedsearch.viewmodels.AbstractFacetOptionViewModelFactory;
import com.commercetools.sunrise.models.search.facetedsearch.viewmodels.FacetOption;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.TermStats;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.time.Duration;
import java.util.*;

import static io.sphere.sdk.client.SphereClientUtils.blockingWait;

@RequestScoped
public class CategoryTreeFacetOptionViewModelFactory extends AbstractFacetOptionViewModelFactory<TermFacetResult, Category, Category> {

    private static final Set<String> IGNORED_PARAMS = Collections.singleton("page");

    private final I18nResolver i18nResolver;
    private final CategoryTree categoryTree;

    @Inject
    public CategoryTreeFacetOptionViewModelFactory(final I18nResolver i18nResolver,
                                                   final CachedCategoryTree cachedCategoryTree) {
        this.i18nResolver = i18nResolver;
        this.categoryTree = blockingWait(cachedCategoryTree.require(), Duration.ofSeconds(30));
    }

    protected final I18nResolver getI18nResolver() {
        return i18nResolver;
    }

    protected final CategoryTree getCategoryTree() {
        return categoryTree;
    }

    @Override
    public final FacetOption create(final TermFacetResult stats, final Category value, @Nullable final Category selectedValue) {
        return super.create(stats, value, selectedValue);
    }

    @Override
    protected final void initialize(final FacetOption viewModel, final TermFacetResult stats, final Category value, @Nullable final Category selectedValue) {
        super.initialize(viewModel, stats, value, selectedValue);
        fillChildren(viewModel, stats, value, selectedValue);
    }

    @Override
    protected void fillLabel(final FacetOption viewModel, final TermFacetResult stats, final Category category, @Nullable final Category selectedValue) {
        viewModel.setLabel(i18nResolver.get(category.getName()).orElseGet(category::getId));
    }

    @Override
    protected void fillValue(final FacetOption viewModel, final TermFacetResult stats, final Category category, @Nullable final Category selectedValue) {
//        productReverseRouter.productOverviewPageCall(category).ifPresent(call ->
//                viewModel.setValue(buildUri(call.url(), extractQueryString(Http.Context.current().request(), IGNORED_PARAMS))));
    }

    @Override
    protected void fillSelected(final FacetOption viewModel, final TermFacetResult stats, final Category category, @Nullable final Category selectedValue) {
        if (selectedValue != null) {
            viewModel.setSelected(category.getId().equals(selectedValue.getId()));
        }
    }

    @Override
    protected void fillCount(final FacetOption viewModel, final TermFacetResult stats, final Category category, @Nullable final Category selectedValue) {
        final Long count = findTermStats(stats, category)
                .map(TermStats::getProductCount)
                .filter(Objects::nonNull)
                .orElse(0L);
        viewModel.setCount(count);
    }

    protected void fillChildren(final FacetOption viewModel, final TermFacetResult stats, final Category category, @Nullable final Category selectedValue) {
        final List<FacetOption> childrenViewModels = new ArrayList<>();
        categoryTree.findChildren(category).forEach(child -> {
            final FacetOption childViewModel = create(stats, child, selectedValue);
            if (childViewModel.getCount() > 0) {
                childrenViewModels.add(childViewModel);
                viewModel.setCount(viewModel.getCount() + childViewModel.getCount());
                viewModel.setSelected(viewModel.isSelected() || childViewModel.isSelected());
            }
        });
        System.out.println(viewModel);
        System.out.println(viewModel.isSelected());
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
