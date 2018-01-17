package com.commercetools.sunrise.models.search.facetedsearch.viewmodels;

import com.commercetools.sunrise.core.viewmodels.ViewModelFactory;

import javax.annotation.Nullable;

public abstract class AbstractFacetOptionViewModelFactory<I, V, S> extends ViewModelFactory {

    protected AbstractFacetOptionViewModelFactory() {
    }

    protected FacetOption newViewModelInstance(final I stats, V value, @Nullable final S selectedValue) {
        return new FacetOption();
    }

    public FacetOption create(final I stats, final V value, @Nullable final S selectedValue) {
        final FacetOption viewModel = newViewModelInstance(stats, value, selectedValue);
        initialize(viewModel, stats, value, selectedValue);
        return viewModel;
    }

    protected void initialize(final FacetOption viewModel, final I stats, final V value, @Nullable final S selectedValue) {
        fillLabel(viewModel, stats, value, selectedValue);
        fillValue(viewModel, stats, value, selectedValue);
        fillSelected(viewModel, stats, value, selectedValue);
        fillCount(viewModel, stats, value, selectedValue);
    }

    protected abstract void fillLabel(final FacetOption viewModel, final I stats, final V value, @Nullable final S selectedValue);

    protected abstract void fillValue(final FacetOption viewModel, final I stats, final V value, @Nullable final S selectedValue);

    protected abstract void fillSelected(final FacetOption viewModel, final I stats, final V value, @Nullable final S selectedValue);

    protected abstract void fillCount(final FacetOption viewModel, final I stats, final V value, @Nullable final S selectedValue);
}
