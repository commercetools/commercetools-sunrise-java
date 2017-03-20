package com.commercetools.sunrise.search.facetedsearch.terms.viewmodels;

import com.commercetools.sunrise.search.facetedsearch.viewmodels.AbstractFacetOptionViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.FacetOptionViewModel;
import io.sphere.sdk.search.TermStats;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class TermFacetOptionViewModelFactory extends AbstractFacetOptionViewModelFactory<TermStats, Void, List<String>> {

    @Override
    public final FacetOptionViewModel create(final TermStats stats, final Void value, @Nullable final List<String> selectedValue) {
        return super.create(stats, value, selectedValue);
    }

    public final FacetOptionViewModel create(final TermStats stats, @Nullable final List<String> selectedValue) {
        return super.create(stats, null, selectedValue);
    }

    @Override
    protected final void initialize(final FacetOptionViewModel viewModel, final TermStats stats, final Void value, @Nullable final List<String> selectedValue) {
        super.initialize(viewModel, stats, value, selectedValue);
    }

    @Override
    protected void fillLabel(final FacetOptionViewModel viewModel, final TermStats stats, final Void value, @Nullable final List<String> selectedValues) {
        viewModel.setLabel(stats.getTerm());
    }

    @Override
    protected void fillValue(final FacetOptionViewModel viewModel, final TermStats stats, final Void value, @Nullable final List<String> selectedValues) {
        viewModel.setValue(stats.getTerm());
    }

    @Override
    protected void fillSelected(final FacetOptionViewModel viewModel, final TermStats stats, final Void value, @Nullable final List<String> selectedValues) {
        if (selectedValues != null) {
            viewModel.setSelected(selectedValues.contains(stats.getTerm()));
        }
    }

    @Override
    protected void fillCount(final FacetOptionViewModel viewModel, final TermStats stats, final Void value, @Nullable final List<String> selectedValues) {
        viewModel.setCount(stats.getProductCount());
    }
}
