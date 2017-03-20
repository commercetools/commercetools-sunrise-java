package com.commercetools.sunrise.search.facetedsearch.sliderranges.viewmodels;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.search.facetedsearch.sliderranges.SliderRangeFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.AbstractFacetSelectorViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.FacetSelectorViewModel;
import io.sphere.sdk.search.model.SimpleRangeStats;

import javax.inject.Inject;

@RequestScoped
public class SliderRangeFacetSelectorViewModelFactory extends AbstractFacetSelectorViewModelFactory<SliderRangeFacetedSearchFormSettings<?>, SimpleRangeStats> {

    private final SliderRangeFacetViewModelFactory sliderRangeFacetViewModelFactory;

    @Inject
    public SliderRangeFacetSelectorViewModelFactory(final SliderRangeFacetViewModelFactory sliderRangeFacetViewModelFactory) {
        this.sliderRangeFacetViewModelFactory = sliderRangeFacetViewModelFactory;
    }

    protected final SliderRangeFacetViewModelFactory getSliderRangeFacetViewModelFactory() {
        return sliderRangeFacetViewModelFactory;
    }

    @Override
    public final FacetSelectorViewModel create(final SliderRangeFacetedSearchFormSettings<?> settings, final SimpleRangeStats facetResult) {
        return super.create(settings, facetResult);
    }

    @Override
    protected final void initialize(final FacetSelectorViewModel viewModel, final SliderRangeFacetedSearchFormSettings<?> settings, final SimpleRangeStats facetResult) {
        super.initialize(viewModel, settings, facetResult);
        fillSliderRangeFacet(viewModel, settings, facetResult);
    }

    @Override
    protected void fillFacet(final FacetSelectorViewModel viewModel, final SliderRangeFacetedSearchFormSettings<?> settings, final SimpleRangeStats facetResult) {
        viewModel.setFacet(sliderRangeFacetViewModelFactory.create(settings, facetResult));
    }

    protected void fillSliderRangeFacet(final FacetSelectorViewModel viewModel, final SliderRangeFacetedSearchFormSettings<?> settings, final SimpleRangeStats facetResult) {
        viewModel.setSliderRangeFacet(true);
    }
}
