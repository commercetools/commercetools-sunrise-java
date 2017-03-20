package io.sphere.sdk.facets;

import io.sphere.sdk.search.*;
import io.sphere.sdk.search.model.FacetedSearchSearchModel;
import io.sphere.sdk.search.model.TermFacetSearchModel;
import io.sphere.sdk.search.model.TermFacetedSearchSearchModel;
import io.sphere.sdk.search.model.TypeSerializer;

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
    @Nullable private final TermFacetResult facetResult;
    @Nullable private final Long threshold;
    @Nullable private final Long limit;
    @Nullable private final FacetOptionMapper mapper;
    private final List<FacetOption> facetOptions;

    protected SelectFacetImpl(final String key, final String label, final boolean countHidden, final FacetType type,
                              final FacetedSearchSearchModel<T> searchModel, final boolean multiSelect, final boolean matchingAll,
                              final List<String> selectedValues, @Nullable final TermFacetResult facetResult,
                              @Nullable final Long threshold, @Nullable final Long limit, @Nullable final FacetOptionMapper mapper) {
        super(key, type, countHidden, label, searchModel);
        if (threshold != null && limit != null && threshold > limit) {
            throw new InvalidSelectFacetConstraintsException(threshold, limit);
        }
        this.multiSelect = multiSelect;
        this.matchingAll = matchingAll;
        this.selectedValues = selectedValues;
        this.facetResult = facetResult;
        this.threshold = threshold;
        this.limit = limit;
        this.mapper = mapper;
        this.facetOptions = initializeOptions(selectedValues, facetResult, mapper);
    }

    @Override
    public boolean isAvailable() {
        return Optional.ofNullable(threshold).map(threshold -> facetOptions.size() >= threshold).orElse(true);
    }

    @Override
    public List<FacetOption> getAllOptions() {
        return facetOptions;
    }

    @Override
    public List<FacetOption> getLimitedOptions() {
        return Optional.ofNullable(limit)
                .map(limit -> facetOptions.stream().limit(limit).collect(toList()))
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

    @Nullable
    @Override
    public TermFacetResult getFacetResult() {
        return facetResult;
    }

    @Nullable
    @Override
    public Long getThreshold() {
        return threshold;
    }

    @Nullable
    @Override
    public Long getLimit() {
        return limit;
    }

    @Nullable
    @Override
    public FacetOptionMapper getMapper() {
        return mapper;
    }

    @Override
    public FacetedSearchExpression<T> getFacetedSearchExpression() {
        return buildFacetedSearchExpression();
    }

    private TermFacetedSearchExpression<T> buildFacetedSearchExpression() {
        final TermFacetExpression<T> facetExpression = buildFacetExpression();
        final List<FilterExpression<T>> filterExpressions = buildFilterExpressions();
        return TermFacetedSearchExpression.of(facetExpression, filterExpressions);
    }

    private List<FilterExpression<T>> buildFilterExpressions() {
        final FacetedSearchSearchModel<T> searchModel = TermFacetedSearchSearchModel.of(getAttributePath());
        final TermFacetedSearchExpression<T> facetedSearchExpr;
        if (selectedValues.isEmpty()) {
            facetedSearchExpr = searchModel.allTerms();
        } else if (isMatchingAll()) {
            facetedSearchExpr = searchModel.containsAll(selectedValues);
        } else {
            facetedSearchExpr = searchModel.containsAny(selectedValues);
        }
        return facetedSearchExpr.filterExpressions();
    }


    private TermFacetExpression<T> buildFacetExpression() {
        final TermFacetSearchModel<T, String> searchModel = TermFacetSearchModel.of(getAttributePath(), TypeSerializer.ofString());
        return searchModel
                .withCountingProducts(!isCountHidden())
                .allTerms();
    }

    private String getAttributePath() {
        return facetedSearchSearchModel.getSearchModel().attributePath();
    }

    @Override
    public SelectFacet<T> withSearchResult(final PagedSearchResult<T> searchResult) {
        final TermFacetResult termFacetResult = searchResult.getFacetResult(facetedSearchSearchModel.allTerms().facetExpression());
        return SelectFacetBuilder.of(this).facetResult(termFacetResult).build();
    }

    @Override
    public Facet<T> withSelectedValues(final List<String> selectedValues) {
        return SelectFacetBuilder.of(this).selectedValues(selectedValues).build();
    }

    @Override
    public SelectFacet<T> withLabel(final String label) {
        return SelectFacetBuilder.of(this).label(label).build();
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
