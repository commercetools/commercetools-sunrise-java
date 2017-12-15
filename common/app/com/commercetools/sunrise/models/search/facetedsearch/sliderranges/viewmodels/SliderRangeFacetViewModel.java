package com.commercetools.sunrise.models.search.facetedsearch.sliderranges.viewmodels;

import com.commercetools.sunrise.models.search.facetedsearch.viewmodels.FacetViewModel;

public class SliderRangeFacetViewModel extends FacetViewModel {

    private SliderRangeEndpointViewModel endpoints;

    public SliderRangeFacetViewModel() {
    }

    public SliderRangeEndpointViewModel getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(final SliderRangeEndpointViewModel endpoints) {
        this.endpoints = endpoints;
    }
}
