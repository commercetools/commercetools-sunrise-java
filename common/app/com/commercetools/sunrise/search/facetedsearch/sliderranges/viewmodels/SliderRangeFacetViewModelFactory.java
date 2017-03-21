package com.commercetools.sunrise.search.facetedsearch.sliderranges.viewmodels;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierResolver;
import com.commercetools.sunrise.search.facetedsearch.sliderranges.SliderRangeFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.AbstractFacetViewModelFactory;
import io.sphere.sdk.search.model.SimpleRangeStats;
import play.mvc.Http;

import javax.inject.Inject;

@RequestScoped
public class SliderRangeFacetViewModelFactory extends AbstractFacetViewModelFactory<SliderRangeFacetViewModel, SliderRangeFacetedSearchFormSettings<?>, SimpleRangeStats> {

    private final Http.Request httpRequest;
    private final SliderRangeEndpointViewModelFactory sliderRangeEndpointViewModelFactory;

    @Inject
    public SliderRangeFacetViewModelFactory(final I18nIdentifierResolver i18nIdentifierResolver, final Http.Context httpContext,
                                            final SliderRangeEndpointViewModelFactory sliderRangeEndpointViewModelFactory) {
        super(i18nIdentifierResolver);
        this.httpRequest = httpContext.request();
        this.sliderRangeEndpointViewModelFactory = sliderRangeEndpointViewModelFactory;
    }

    protected final Http.Request getHttpRequest() {
        return httpRequest;
    }

    protected final SliderRangeEndpointViewModelFactory getSliderRangeEndpointViewModelFactory() {
        return sliderRangeEndpointViewModelFactory;
    }

    @Override
    protected SliderRangeFacetViewModel newViewModelInstance(final SliderRangeFacetedSearchFormSettings<?> settings, final SimpleRangeStats rangeStats) {
        return new SliderRangeFacetViewModel();
    }

    @Override
    public final SliderRangeFacetViewModel create(final SliderRangeFacetedSearchFormSettings<?> settings, final SimpleRangeStats rangeStats) {
        return super.create(settings, rangeStats);
    }

    @Override
    protected final void initialize(final SliderRangeFacetViewModel viewModel, final SliderRangeFacetedSearchFormSettings<?> settings, final SimpleRangeStats rangeStats) {
        super.initialize(viewModel, settings, rangeStats);
        fillEndpoints(viewModel, settings, rangeStats);
    }

    @Override
    protected void fillAvailable(final SliderRangeFacetViewModel viewModel, final SliderRangeFacetedSearchFormSettings<?> settings, final SimpleRangeStats rangeStats) {
        viewModel.setAvailable(rangeStats.getCount() > 0);
    }

    protected void fillEndpoints(final SliderRangeFacetViewModel viewModel, final SliderRangeFacetedSearchFormSettings<?> settings, final SimpleRangeStats rangeStats) {
        viewModel.setEndpoints(sliderRangeEndpointViewModelFactory.create(settings, rangeStats));
    }
}
