package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermFacetResult;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

abstract class BaseSelectFacetUiData<T> extends BaseFacetUiData {
    protected final List<T> selectedValues;
    private final TermFacetResult<T> facetResult;
    private final Optional<Long> threshold;
    private final Optional<Long> limit;

    protected BaseSelectFacetUiData(final String key, final String label, final TermFacetResult<T> facetResult, final List<T> selectedValues,
                                    @Nullable final Long threshold, @Nullable final Long limit) {
        super(key, label);
        if (threshold != null && limit != null && threshold > limit) {
            throw new InvalidSelectFacetConstraintsException(threshold, limit);
        }
        this.facetResult = facetResult;
        this.selectedValues = selectedValues;
        this.threshold = Optional.ofNullable(threshold);
        this.limit = Optional.ofNullable(limit);
    }

    @Override
    public TermFacetResult<T> getFacetResult() {
        return facetResult;
    }

    @Override
    public boolean canBeDisplayed() {
        return threshold.map(threshold -> getAllTermsUiData().size() >= threshold).orElse(true);
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
        return limit
                .map(limit -> getAllTermsUiData().stream().limit(limit).collect(toList()))
                .orElse(getAllTermsUiData());
    }

    /**
     * Gets the threshold indicating the minimum amount of terms allowed to be displayed in the facet UI data.
     * @return the threshold for the amount of terms that can be displayed, or absent if it has no threshold
     */
    public Optional<Long> getThreshold() {
        return threshold;
    }

    /**
     * Gets the limit for the maximum amount of terms allowed to be displayed in the facet UI data.
     * @return the limit for the amount of terms that can be displayed, or absent if it has no limit
     */
    public Optional<Long> getLimit() {
        return limit;
    }

    protected static <T> List<TermUiData<T>> toTermsUiData(final TermFacetResult<T> facetResult, final List<T> selectedValues) {
        return facetResult.getTerms().stream()
                .map(termStat -> TermUiData.of(termStat, selectedValues))
                .collect(toList());
    }
}
