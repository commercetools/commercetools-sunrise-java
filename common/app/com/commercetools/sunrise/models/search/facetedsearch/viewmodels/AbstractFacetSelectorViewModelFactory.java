package com.commercetools.sunrise.models.search.facetedsearch.viewmodels;

import com.commercetools.sunrise.core.viewmodels.ViewModelFactory;
import com.commercetools.sunrise.models.search.facetedsearch.FacetedSearchFormSettings;

public abstract class AbstractFacetSelectorViewModelFactory<T extends FacetedSearchFormSettings<?>, F> extends ViewModelFactory {

    protected FacetSelectorViewModel newViewModelInstance(final T settings, final F facetResult) {
        return new FacetSelectorViewModel();
    }

    public FacetSelectorViewModel create(final T settings, final F facetResult) {
        final FacetSelectorViewModel viewModel = newViewModelInstance(settings, facetResult);
        initialize(viewModel, settings, facetResult);
        return viewModel;
    }

    protected void initialize(final FacetSelectorViewModel viewModel, final T settings, final F facetResult) {
        fillFacet(viewModel, settings, facetResult);
    }

    protected abstract void fillFacet(final FacetSelectorViewModel viewModel, final T settings, final F facetResult);
}
