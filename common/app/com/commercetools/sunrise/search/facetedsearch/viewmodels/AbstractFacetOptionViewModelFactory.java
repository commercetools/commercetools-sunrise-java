package com.commercetools.sunrise.search.facetedsearch.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.ViewModelFactory;

import javax.annotation.Nullable;

public abstract class AbstractFacetOptionViewModelFactory<I, V, S> extends ViewModelFactory {

    protected AbstractFacetOptionViewModelFactory() {
    }

    protected FacetOptionViewModel newViewModelInstance(final I stats, V value, @Nullable final S selectedValue) {
        return new FacetOptionViewModel();
    }

    public FacetOptionViewModel create(final I stats, final V value, @Nullable final S selectedValue) {
        final FacetOptionViewModel viewModel = newViewModelInstance(stats, value, selectedValue);
        initialize(viewModel, stats, value, selectedValue);
        return viewModel;
    }

    protected void initialize(final FacetOptionViewModel viewModel, final I stats, final V value, @Nullable final S selectedValue) {
        fillLabel(viewModel, stats, value, selectedValue);
        fillValue(viewModel, stats, value, selectedValue);
        fillSelected(viewModel, stats, value, selectedValue);
        fillCount(viewModel, stats, value, selectedValue);
    }

    protected abstract void fillLabel(final FacetOptionViewModel viewModel, final I stats, final V value, @Nullable final S selectedValue);

    protected abstract void fillValue(final FacetOptionViewModel viewModel, final I stats, final V value, @Nullable final S selectedValue);

    protected abstract void fillSelected(final FacetOptionViewModel viewModel, final I stats, final V value, @Nullable final S selectedValue);

    protected abstract void fillCount(final FacetOptionViewModel viewModel, final I stats, final V value, @Nullable final S selectedValue);
}
