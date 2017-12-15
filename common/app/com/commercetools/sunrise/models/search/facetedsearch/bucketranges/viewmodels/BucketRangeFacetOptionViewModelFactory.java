package com.commercetools.sunrise.models.search.facetedsearch.bucketranges.viewmodels;

import com.commercetools.sunrise.core.i18n.I18nResolver;
import com.commercetools.sunrise.core.injection.RequestScoped;
import com.commercetools.sunrise.models.search.facetedsearch.bucketranges.BucketRangeFacetedSearchFormOption;
import com.commercetools.sunrise.models.search.facetedsearch.viewmodels.AbstractFacetOptionViewModelFactory;
import com.commercetools.sunrise.models.search.facetedsearch.viewmodels.FacetOptionViewModel;
import io.sphere.sdk.search.model.RangeStats;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;

@RequestScoped
public class BucketRangeFacetOptionViewModelFactory extends AbstractFacetOptionViewModelFactory<RangeStats, BucketRangeFacetedSearchFormOption, List<String>> {

    private final I18nResolver i18nResolver;

    @Inject
    public BucketRangeFacetOptionViewModelFactory(final I18nResolver i18nResolver) {
        this.i18nResolver = i18nResolver;
    }

    protected final I18nResolver getI18nResolver() {
        return i18nResolver;
    }

    @Override
    public final FacetOptionViewModel create(final RangeStats stats, final BucketRangeFacetedSearchFormOption value, @Nullable final List<String> selectedValue) {
        return super.create(stats, value, selectedValue);
    }

    @Override
    protected final void initialize(final FacetOptionViewModel viewModel, final RangeStats stats, final BucketRangeFacetedSearchFormOption value, @Nullable final List<String> selectedValue) {
        super.initialize(viewModel, stats, value, selectedValue);
    }

    @Override
    protected void fillLabel(final FacetOptionViewModel viewModel, final RangeStats stats, final BucketRangeFacetedSearchFormOption option, @Nullable final List<String> selectedValues) {
        viewModel.setLabel(i18nResolver.getOrKey(option.getFieldLabel()));
    }

    @Override
    protected void fillValue(final FacetOptionViewModel viewModel, final RangeStats stats, final BucketRangeFacetedSearchFormOption option, @Nullable final List<String> selectedValues) {
        viewModel.setValue(option.getFieldValue());
    }

    @Override
    protected void fillSelected(final FacetOptionViewModel viewModel, final RangeStats stats, final BucketRangeFacetedSearchFormOption option, @Nullable final List<String> selectedValues) {
        if (selectedValues != null) {
            viewModel.setSelected(selectedValues.contains(option.getFieldValue()));
        }
    }

    @Override
    protected void fillCount(final FacetOptionViewModel viewModel, final RangeStats stats, final BucketRangeFacetedSearchFormOption option, @Nullable final List<String> selectedValue) {
        viewModel.setCount(stats.getProductCount());
    }
}
