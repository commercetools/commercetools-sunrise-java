package com.commercetools.sunrise.search.facetedsearch.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;

public class FacetSelectorViewModel extends ViewModel {

    private FacetViewModel facet;
    private boolean selectFacet;
    private boolean sliderRangeFacet;
    private boolean bucketRangeFacet;
    private boolean displayList;
    private boolean hierarchicalSelectFacet;

    public FacetSelectorViewModel() {
    }

    public FacetViewModel getFacet() {
        return facet;
    }

    public void setFacet(final FacetViewModel facet) {
        this.facet = facet;
    }

    public boolean isSelectFacet() {
        return selectFacet;
    }

    public void setSelectFacet(final boolean selectFacet) {
        this.selectFacet = selectFacet;
    }

    public boolean isSliderRangeFacet() {
        return sliderRangeFacet;
    }

    public void setSliderRangeFacet(final boolean sliderRangeFacet) {
        this.sliderRangeFacet = sliderRangeFacet;
    }

    public boolean isBucketRangeFacet() {
        return bucketRangeFacet;
    }

    public void setBucketRangeFacet(final boolean bucketRangeFacet) {
        this.bucketRangeFacet = bucketRangeFacet;
    }

    public boolean isDisplayList() {
        return displayList;
    }

    public void setDisplayList(final boolean displayList) {
        this.displayList = displayList;
    }

    public boolean isHierarchicalSelectFacet() {
        return hierarchicalSelectFacet;
    }

    public void setHierarchicalSelectFacet(final boolean hierarchicalSelectFacet) {
        this.hierarchicalSelectFacet = hierarchicalSelectFacet;
    }
}
