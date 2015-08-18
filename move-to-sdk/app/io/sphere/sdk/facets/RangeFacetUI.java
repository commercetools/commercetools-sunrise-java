package io.sphere.sdk.facets;

import io.sphere.sdk.search.RangeFacetResult;

public class RangeFacetUI<T extends Comparable<? super T>> extends BaseFacetUI {
    private final T minSelectedValue;
    private final T maxSelectedValue;
    private final RangeFacetResult<T> facetResult;
    private final long unit;

    protected RangeFacetUI(final String key, final String label, final RangeFacetResult<T> facetResult,
                           final T minSelectedValue, final T maxSelectedValue, final long unit) {
        super(key, label);
        this.minSelectedValue = minSelectedValue;
        this.maxSelectedValue = maxSelectedValue;
        this.facetResult = facetResult;
        this.unit = unit;
    }

    @Override
    public RangeFacetResult<T> getFacetResult() {
        return facetResult;
    }

    @Override
    public boolean canBeDisplayed() {
        return !facetResult.getRanges().isEmpty();
    }

    public T getMinSelectedValue() {
        return minSelectedValue;
    }

    public T getMaxSelectedValue() {
        return maxSelectedValue;
    }

    public long getUnit() {
        return unit;
    }

    public static <T extends Comparable<? super T>> RangeFacetUI<T> of(final String key, final String label,
                                                                       final T minSelectedValue, final T maxSelectedValue,
                                                                       final RangeFacetResult<T> facetResult, final long unit) {
        return new RangeFacetUI<>(key, label, facetResult, minSelectedValue, maxSelectedValue, unit);
    }
}
