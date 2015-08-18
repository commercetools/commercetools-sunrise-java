package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermFacetResult;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

abstract class BaseSelectFacetUI<T> extends BaseFacetUI {
    protected final List<T> selectedValues;
    private final TermFacetResult<T> facetResult;
    private final Optional<Long> termsThreshold;
    private final Optional<Long> termsLimit;

    protected BaseSelectFacetUI(final String key, final String label, final TermFacetResult<T> facetResult, final List<T> selectedValues,
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
        return termsThreshold.map(threshold -> getAllTermsUI().size() >= threshold).orElse(true);
    }

    /**
     * Gets the whole terms UI without restrictions.
     * @return the term list UI
     */
    public abstract List<TermUI<T>> getAllTermsUI();

    /**
     * Obtains the truncated term UI list according to the defined term limit.
     * @return the truncated term list if the limit is defined, the whole term list otherwise
     */
    public List<TermUI<T>> getLimitedTermsUI() {
        return termsLimit
                .map(limit -> getAllTermsUI().stream().limit(limit).collect(toList()))
                .orElse(getAllTermsUI());
    }

    /**
     * Gets the threshold indicating the minimum amount of terms allowed to be displayed in the facet UI
     * @return the threshold for the amount of terms that can be displayed, or absent if it has no threshold
     */
    public Optional<Long> getTermsThreshold() {
        return termsThreshold;
    }

    /**
     * Gets the limit for the maximum amount of terms allowed to be displayed in the facet UI
     * @return the limit for the amount of terms that can be displayed, or absent if it has no limit
     */
    public Optional<Long> getTermsLimit() {
        return termsLimit;
    }

    protected static <T> List<TermUI<T>> toTermsUI(final TermFacetResult<T> facetResult, final List<T> selectedValues) {
        return facetResult.getTerms().stream()
                .map(termStat -> TermUI.of(termStat, selectedValues))
                .collect(toList());
    }
}
