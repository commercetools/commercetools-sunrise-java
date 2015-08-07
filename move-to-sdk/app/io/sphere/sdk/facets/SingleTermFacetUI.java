package io.sphere.sdk.facets;

import io.sphere.sdk.search.FacetExpression;
import io.sphere.sdk.search.TermFacetResult;

import java.util.Optional;

public class SingleTermFacetUI<T, V> extends BaseFacetUI<T> {
    private final TermFacetResult<V> facetResult;
    private final long termsThreshold;
    private final Optional<Long> termsLimit;

    protected SingleTermFacetUI(final String key, final String label, final FacetExpression<T> expression,
                             final TermFacetResult<V> facetResult, final long termsThreshold, final Optional<Long> termsLimit) {
        super(key, label, expression);
        this.facetResult = facetResult;
        this.termsThreshold = termsThreshold;
        this.termsLimit = termsLimit;
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

    /**
     * Gets the term facet result concerning a particular attribute for this facet UI representation
     * @return the term facet result for this facet UI
     */
    public TermFacetResult<V> getFacetResult() {
        return facetResult;
    }

    /**
     * Gets the threshold indicating the minimum amount of terms allowed to be displayed in the facet UI
     * @return the threshold for the amount of terms that can be displayed,
     */
    public long getTermsThreshold() {
        return termsThreshold;
    }

    /**
     * Gets the limit for the maximum amount of terms allowed to be displayed in the facet UI
     * @return the limit for the amount of terms that can be displayed, or absent if it has no limit
     */
    public Optional<Long> getTermsLimit() {
        return termsLimit;
    }

    @Override
    public boolean canBeDisplayed() {
        return facetResult.getTerms().size() >= termsThreshold;
    }
}
