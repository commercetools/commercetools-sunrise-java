package io.sphere.sdk.facets;

import io.sphere.sdk.search.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

abstract class BaseSelectFacet<T> extends BaseFacet<T> implements SelectFacet<T> {
    private final boolean multiSelect;
    private final boolean matchingAll;
    private final List<String> selectedValues;
    private final Optional<TermFacetResult> facetResult;
    private final Optional<Long> threshold;
    private final Optional<Long> limit;

    protected BaseSelectFacet(final String key, final String label, final FacetType type, final UntypedSearchModel<T> searchModel,
                              final boolean multiSelect, final boolean matchingAll, final List<String> selectedValues,
                              @Nullable final TermFacetResult facetResult, @Nullable final Long threshold, @Nullable final Long limit) {
        super(key, label, type, searchModel);
        if (threshold != null && limit != null && threshold > limit) {
            throw new InvalidSelectFacetConstraintsException(threshold, limit);
        }
        this.multiSelect = multiSelect;
        this.matchingAll = matchingAll;
        this.selectedValues = selectedValues;
        this.facetResult = Optional.ofNullable(facetResult);
        this.threshold = Optional.ofNullable(threshold);
        this.limit = Optional.ofNullable(limit);
    }

    /**
     * Generates the facet options according to the facet result and selected values provided.
     * @return the generated facet options
     */
    protected List<FacetOption> getOptions() {
        return facetResult
                .map(result -> result.getTerms().stream()
                        .map(termStats -> FacetOption.ofTermStats(termStats, selectedValues))
                        .collect(toList()))
                .orElse(emptyList());
    }

    @Override
    public boolean isAvailable() {
        return threshold.map(threshold -> getOptions().size() >= threshold).orElse(true);
    }

    @Override
    public List<FacetOption> getAllOptions() {
        return getOptions();
    }

    @Override
    public List<FacetOption> getLimitedOptions() {
        return limit.map(limit -> getOptions().stream().limit(limit).collect(toList()))
                .orElse(getOptions());
    }

    @Override
    public boolean isMultiSelect() {
        return multiSelect;
    }

    @Override
    public boolean isMatchingAll() {
        return matchingAll;
    }

    @Override
    public List<String> getSelectedValues() {
        return selectedValues;
    }

    @Override
    public Optional<TermFacetResult> getFacetResult() {
        return facetResult;
    }

    @Override
    public Optional<Long> getThreshold() {
        return threshold;
    }

    @Override
    public Optional<Long> getLimit() {
        return limit;
    }

    @Override
    public List<FilterExpression<T>> getFilterExpressions() {
        final List<FilterExpression<T>> filterExpressions;
        if (selectedValues.isEmpty()) {
            filterExpressions = emptyList();
        } else {
            if (matchingAll) {
                filterExpressions = selectedValues.stream()
                        .map(selectedValue -> searchModel.filtered().by(selectedValue))
                        .collect(toList());
            } else {
                filterExpressions = singletonList(searchModel.filtered().by(selectedValues));
            }
        }
        return filterExpressions;
    }

    @Override
    public FacetExpression<T> getFacetExpression() {
        return searchModel.faceted().byAllTerms();
    }
}
