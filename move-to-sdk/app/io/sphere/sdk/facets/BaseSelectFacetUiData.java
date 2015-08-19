package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermFacetResult;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

abstract class BaseSelectFacetUiData<T> extends BaseFacetUiData {
    protected final List<T> selectedValues;
    private final TermFacetResult<T> facetResult;
    private final Optional<Long> termsThreshold;
    private final Optional<Long> termsLimit;

    protected BaseSelectFacetUiData(final String key, final String label, final TermFacetResult<T> facetResult, final List<T> selectedValues,
                                    @Nullable final Long termsThreshold, @Nullable final Long termsLimit) {
        super(key, label);
        if (termsThreshold != null && termsLimit != null && termsThreshold > termsLimit) {
            throw new InvalidSelectFacetConstraintsException(termsThreshold, termsLimit);
        }
        this.facetResult = facetResult;
        this.selectedValues = selectedValues;
        this.termsThreshold = Optional.ofNullable(termsThreshold);
        this.termsLimit = Optional.ofNullable(termsLimit);
    }

    @Override
    public TermFacetResult<T> getFacetResult() {
        return facetResult;
    }

    @Override
    public boolean canBeDisplayed() {
        return termsThreshold.map(threshold -> getAllTermsUiData().size() >= threshold).orElse(true);
    }

    /**
     * Gets the whole terms UI data without restrictions.
     * @return the term list UI data
     */
    public abstract List<TermUiData<T>> getAllTermsUiData();

    /**
     * Obtains the truncated term UI data list according to the defined term limit.
     * @return the truncated term list if the limit is defined, the whole term list otherwise
     */
    public List<TermUiData<T>> getLimitedTermsUiData() {
        return termsLimit
                .map(limit -> getAllTermsUiData().stream().limit(limit).collect(toList()))
                .orElse(getAllTermsUiData());
    }

    /**
     * Gets the threshold indicating the minimum amount of terms allowed to be displayed in the facet UI data.
     * @return the threshold for the amount of terms that can be displayed, or absent if it has no threshold
     */
    public Optional<Long> getTermsThreshold() {
        return termsThreshold;
    }

    /**
     * Gets the limit for the maximum amount of terms allowed to be displayed in the facet UI data.
     * @return the limit for the amount of terms that can be displayed, or absent if it has no limit
     */
    public Optional<Long> getTermsLimit() {
        return termsLimit;
    }

    protected static <T> List<TermUiData<T>> toTermsUiData(final TermFacetResult<T> facetResult, final List<T> selectedValues) {
        return facetResult.getTerms().stream()
                .map(termStat -> TermUiData.of(termStat, selectedValues))
                .collect(toList());
    }
}
