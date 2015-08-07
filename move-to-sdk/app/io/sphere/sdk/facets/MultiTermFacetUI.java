package io.sphere.sdk.facets;

import io.sphere.sdk.search.FacetExpression;
import io.sphere.sdk.search.TermFacetResult;

import java.util.Optional;

public class MultiTermFacetUI<T, V> extends SingleTermFacetUI<T, V> implements MultiSelectFacet {
    private final boolean matchesAll;

    protected MultiTermFacetUI(final String key, final String label, final FacetExpression<T> expression,
                            final TermFacetResult<V> facetResult, final long termsThreshold, final Optional<Long> termsLimit,
                            final boolean matchesAll) {
        super(key, label, expression, facetResult, termsThreshold, termsLimit);
        this.matchesAll = matchesAll;
    }

    @Override
    public String getKey() {
        return super.getKey();
    }

    @Override
    public String getLabel() {
        return super.getLabel();
    }

    @Override
    public FacetExpression<T> getExpression() {
        return super.getExpression();
    }

    @Override
    public TermFacetResult<V> getFacetResult() {
        return super.getFacetResult();
    }

    @Override
    public long getTermsThreshold() {
        return super.getTermsThreshold();
    }

    @Override
    public Optional<Long> getTermsLimit() {
        return super.getTermsLimit();
    }

    @Override
    public boolean canBeDisplayed() {
        return super.canBeDisplayed();
    }

    @Override
    public boolean matchesAll() {
        return matchesAll;
    }
}
