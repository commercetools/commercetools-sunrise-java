package com.commercetools.sunrise.search.facetedsearch.sliderranges.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;

public class SliderRangeEndpointViewModel extends ViewModel {

    private String lowerName;
    private String lowerValue;
    private String lowerEndpoint;
    private String upperName;
    private String upperValue;
    private String upperEndpoint;
    private long count;

    public SliderRangeEndpointViewModel() {
    }

    public String getLowerName() {
        return lowerName;
    }

    public void setLowerName(final String lowerName) {
        this.lowerName = lowerName;
    }

    public String getLowerValue() {
        return lowerValue;
    }

    public void setLowerValue(final String lowerValue) {
        this.lowerValue = lowerValue;
    }

    public String getLowerEndpoint() {
        return lowerEndpoint;
    }

    public void setLowerEndpoint(final String lowerEndpoint) {
        this.lowerEndpoint = lowerEndpoint;
    }

    public String getUpperName() {
        return upperName;
    }

    public void setUpperName(final String upperName) {
        this.upperName = upperName;
    }

    public String getUpperValue() {
        return upperValue;
    }

    public void setUpperValue(final String upperValue) {
        this.upperValue = upperValue;
    }

    public String getUpperEndpoint() {
        return upperEndpoint;
    }

    public void setUpperEndpoint(final String upperEndpoint) {
        this.upperEndpoint = upperEndpoint;
    }

    public long getCount() {
        return count;
    }

    public void setCount(final long count) {
        this.count = count;
    }
}
