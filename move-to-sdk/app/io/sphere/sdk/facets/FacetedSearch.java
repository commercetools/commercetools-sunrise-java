package io.sphere.sdk.facets;

import io.sphere.sdk.search.*;
import play.mvc.Http;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

/**
 * @param <T> type of the search result
 */
public class FacetedSearch<T>  {
    private final List<SelectFacet<T>> boundSelectFacets;

    private FacetedSearch(final List<SelectFacet<T>> boundSelectFacets) {
        this.boundSelectFacets = boundSelectFacets;
    }

    public List<SelectFacet<T>> getBoundSelectFacets() {
        return boundSelectFacets;
    }

    /**
     *
     * @param searchRequest
     * @param <S> type of the search request
     * @param <M> type of the search model
     * @param <E> type of the expansion model
     * @return
     */
    public <S extends MetaModelSearchDsl<T, S, M, E>, M, E> S applyRequest(final S searchRequest) {
        S baseSearchRequest = searchRequest;
        for (final SelectFacet<T> facet : boundSelectFacets) {
            baseSearchRequest = applyRequest(baseSearchRequest, facet);
        }
        return baseSearchRequest;
    }

    public List<SelectFacet<T>> applyResult(final PagedSearchResult<T> searchResult) {
        return boundSelectFacets.stream().map(facet -> {
            final TermFacetResult result = searchResult.getTermFacetResult(facetExpression(facet));
            return facet.withTermFacetResult(result);
        }).collect(toList());
    }

    /**
     *
     * @param searchRequest
     * @param facet
     * @param <S> type of the search request
     * @param <M> type of the search model
     * @param <E> type of the expansion model
     * @return
     */
    private <S extends MetaModelSearchDsl<T, S, M, E>, M, E> S applyRequest(final S searchRequest, final SelectFacet<T> facet) {
        final List<FilterExpression<T>> filterExpressions = filterExpressions(facet);
        return searchRequest
                .plusFacets(facetExpression(facet))
                .plusFacetFilters(filterExpressions)
                .plusResultFilters(filterExpressions);
    }

    private List<FilterExpression<T>> filterExpressions(final SelectFacet<T> facet) {
        final List<FilterExpression<T>> filterExpressions;
        final List<String> selectedValues = facet.getSelectedValues();
        if (selectedValues.isEmpty()) {
            filterExpressions = emptyList();
        } else {
            if (facet.isMatchingAll()) {
                filterExpressions = selectedValues.stream()
                        .map(selectedValue -> facet.getSearchModel().filtered().by(selectedValue))
                        .collect(toList());
            } else {
                filterExpressions = singletonList(facet.getSearchModel().filtered().by(selectedValues));
            }
        }
        return filterExpressions;
    }

    private TermFacetExpression<T, String> facetExpression(final SelectFacet<T> facet) {
        return facet.getSearchModel().faceted().byAllTerms();
    }

    public static <T> FacetedSearch<T> of(final Http.Request request, final List<SelectFacet<T>> facets) {
        final List<SelectFacet<T>> selectFacets = bindFacetsWithRequest(request, facets);
        return new FacetedSearch<>(selectFacets);
    }

    public static <T> List<SelectFacet<T>> bindFacetsWithRequest(final Http.Request request, final List<SelectFacet<T>> facets) {
        return facets.stream().map(facet -> {
            final List<String> selectedValues = asList(request.queryString().getOrDefault(facet.getKey(), new String[0]));
            return facet.withSelectedValues(selectedValues);
        }).collect(toList());
    }
}
