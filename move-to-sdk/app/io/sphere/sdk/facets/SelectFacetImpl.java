package io.sphere.sdk.facets;

import io.sphere.sdk.search.PagedSearchResult;
import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.TermFacetedSearchExpression;
import io.sphere.sdk.search.model.TermFacetedSearchSearchModel;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Select facet that allows to manipulate the facet options via a FacetOptionMapper. This enables any desired behaviour,
 * such as replacing the values for its localized names or building hierarchies out of the flat list of options received
 * from the search result.
 * As this facet has no particular type, it is required to also provide the associated facet type.
 * @param <T> type of the resource for this facet (e.g. ProductProjection)
 */
public class SelectFacetImpl<T> extends BaseFacet<T> implements SelectFacet<T> {
    private final boolean multiSelect;
    private final boolean matchingAll;
    private final List<String> selectedValues;
    private final Optional<TermFacetResult> facetResult;
    private final Optional<Long> threshold;
    private final Optional<Long> limit;
    private final Optional<FacetOptionMapper> mapper;
    private final List<FacetOption> facetOptions;

    protected SelectFacetImpl(final String key, final String label, final boolean countHidden, final FacetType type,
                              final TermFacetedSearchSearchModel<T> searchModel, final boolean multiSelect, final boolean matchingAll,
                              final List<String> selectedValues, @Nullable final TermFacetResult facetResult,
                              @Nullable final Long threshold, @Nullable final Long limit, @Nullable final FacetOptionMapper mapper) {
        super(key, label, countHidden, type, searchModel);
        if (threshold != null && limit != null && threshold > limit) {
            throw new InvalidSelectFacetConstraintsException(threshold, limit);
        }
        this.multiSelect = multiSelect;
        this.matchingAll = matchingAll;
        this.selectedValues = selectedValues;
        this.facetResult = Optional.ofNullable(facetResult);
        this.threshold = Optional.ofNullable(threshold);
        this.limit = Optional.ofNullable(limit);
        this.mapper = Optional.ofNullable(mapper);
        this.facetOptions = initializeOptions(selectedValues, facetResult, mapper);
    }

    @Override
    public boolean isAvailable() {
        return threshold.map(threshold -> facetOptions.size() >= threshold).orElse(true);
    }

    @Override
    public List<FacetOption> getAllOptions() {
        return facetOptions;
    }

    @Override
    public List<FacetOption> getLimitedOptions() {
        return limit.map(limit -> facetOptions.stream().limit(limit).collect(toList()))
                .orElse(facetOptions);
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
    public Optional<FacetOptionMapper> getMapper() {
        return mapper;
    }

    @Override
    public TermFacetedSearchExpression<T> getFacetedSearchExpression() {
        final TermFacetedSearchExpression<T> facetedSearchExpr;
        if (selectedValues.isEmpty()) {
            facetedSearchExpr = searchModel.allTerms();
        } else if (matchingAll) {
            facetedSearchExpr = searchModel.containsAll(selectedValues);
        } else {
            facetedSearchExpr = searchModel.containsAny(selectedValues);
        }
        return facetedSearchExpr;
    }

    @Override
    public SelectFacet<T> withSearchResult(final PagedSearchResult<T> searchResult) {
        final TermFacetResult termFacetResult = searchResult.getFacetResult(searchModel.allTerms().facetExpression());
        return SelectFacetBuilder.of(this).facetResult(termFacetResult).build();
    }

    @Override
    public SelectFacet<T> withSelectedValues(final List<String> selectedValues) {
        return SelectFacetBuilder.of(this).selectedValues(selectedValues).build();
    }

    /**
     * Generates the facet options according to the facet result and selected values provided.
     * @return the generated facet options
     */
    private static List<FacetOption> initializeOptions(final List<String> selectedValues, @Nullable final TermFacetResult facetResult,
                                                       @Nullable final FacetOptionMapper mapper) {
        final List<FacetOption> facetOptions = Optional.ofNullable(facetResult)
                .map(result -> result.getTerms().stream()
                        .map(termStats -> FacetOption.ofTermStats(termStats, selectedValues))
                        .collect(toList()))
                .orElseGet(Collections::emptyList);
        return Optional.ofNullable(mapper).map(m -> m.apply(facetOptions)).orElse(facetOptions);
    }
}
