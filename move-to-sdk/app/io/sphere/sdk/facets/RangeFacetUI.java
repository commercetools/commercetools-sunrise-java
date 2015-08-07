package io.sphere.sdk.facets;

import io.sphere.sdk.search.FacetExpression;
import io.sphere.sdk.search.RangeFacetResult;

public class RangeFacetUI<T, V> extends BaseFacetUI<T> {
    private final RangeFacetResult<V> facetResult;
    private final long unit;

    public RangeFacetUI(final String key, final String label, final FacetExpression<T> expression,
                        final RangeFacetResult<V> facetResult, final long unit) {
        super(key, label, expression);
        this.facetResult = facetResult;
        this.unit = unit;
    }

    public RangeFacetResult<V> getFacetResult() {
        return facetResult;
    }

    public long getUnit() {
        return unit;
    }

    @Override
    public boolean canBeDisplayed() {
        return !facetResult.getRanges().isEmpty();
    }
}
