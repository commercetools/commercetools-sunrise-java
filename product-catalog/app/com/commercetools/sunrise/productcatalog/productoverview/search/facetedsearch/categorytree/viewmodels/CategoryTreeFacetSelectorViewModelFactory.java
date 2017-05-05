package com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.categorytree.viewmodels;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.categorytree.CategoryTreeFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.AbstractFacetSelectorViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.FacetSelectorViewModel;
import io.sphere.sdk.search.TermFacetResult;

import javax.inject.Inject;

@RequestScoped
public class CategoryTreeFacetSelectorViewModelFactory extends AbstractFacetSelectorViewModelFactory<CategoryTreeFacetedSearchFormSettings, TermFacetResult> {

    private final CategoryTreeFacetViewModelFactory categoryTreeFacetViewModelFactory;

    @Inject
    public CategoryTreeFacetSelectorViewModelFactory(final CategoryTreeFacetViewModelFactory categoryTreeFacetViewModelFactory) {
        this.categoryTreeFacetViewModelFactory = categoryTreeFacetViewModelFactory;
    }

    protected final CategoryTreeFacetViewModelFactory getCategoryTreeFacetViewModelFactory() {
        return categoryTreeFacetViewModelFactory;
    }

    @Override
    public final FacetSelectorViewModel create(final CategoryTreeFacetedSearchFormSettings settings, final TermFacetResult facetResult) {
        return super.create(settings, facetResult);
    }

    @Override
    protected final void initialize(final FacetSelectorViewModel viewModel, final CategoryTreeFacetedSearchFormSettings settings, final TermFacetResult facetResult) {
        super.initialize(viewModel, settings, facetResult);
        fillHierarchicalSelectFacet(viewModel, settings, facetResult);
    }

    @Override
    protected void fillFacet(final FacetSelectorViewModel viewModel, final CategoryTreeFacetedSearchFormSettings settings, final TermFacetResult facetResult) {
        viewModel.setFacet(categoryTreeFacetViewModelFactory.create(settings, facetResult));
    }

    protected void fillHierarchicalSelectFacet(final FacetSelectorViewModel viewModel, final CategoryTreeFacetedSearchFormSettings settings, final TermFacetResult facetResult) {
        viewModel.setHierarchicalSelectFacet(true);
    }
}
