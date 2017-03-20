package com.commercetools.sunrise.search.facetedsearch.bucketranges.viewmodels;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.search.facetedsearch.bucketranges.BucketRangeFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.AbstractFacetSelectorViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.FacetSelectorViewModel;
import io.sphere.sdk.search.RangeFacetResult;

import javax.inject.Inject;

@RequestScoped
public class BucketRangeFacetSelectorViewModelFactory extends AbstractFacetSelectorViewModelFactory<BucketRangeFacetedSearchFormSettings<?>, RangeFacetResult> {

    private final BucketRangeFacetViewModelFactory bucketRangeFacetViewModelFactory;

    @Inject
    public BucketRangeFacetSelectorViewModelFactory(final BucketRangeFacetViewModelFactory bucketRangeFacetViewModelFactory) {
        this.bucketRangeFacetViewModelFactory = bucketRangeFacetViewModelFactory;
    }

    protected final BucketRangeFacetViewModelFactory getBucketRangeFacetViewModelFactory() {
        return bucketRangeFacetViewModelFactory;
    }

    @Override
    public final FacetSelectorViewModel create(final BucketRangeFacetedSearchFormSettings<?> settings, final RangeFacetResult facetResult) {
        return super.create(settings, facetResult);
    }

    @Override
    protected final void initialize(final FacetSelectorViewModel viewModel, final BucketRangeFacetedSearchFormSettings<?> settings, final RangeFacetResult facetResult) {
        super.initialize(viewModel, settings, facetResult);
        fillBucketRangeFacet(viewModel, settings, facetResult);
    }

    @Override
    protected void fillFacet(final FacetSelectorViewModel viewModel, final BucketRangeFacetedSearchFormSettings<?> settings, final RangeFacetResult facetResult) {
        viewModel.setFacet(bucketRangeFacetViewModelFactory.create(settings, facetResult));
    }

    protected void fillBucketRangeFacet(final FacetSelectorViewModel viewModel, final BucketRangeFacetedSearchFormSettings<?> settings, final RangeFacetResult facetResult) {
        viewModel.setBucketRangeFacet(true);
    }
}
