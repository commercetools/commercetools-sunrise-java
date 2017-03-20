package com.commercetools.sunrise.search.facetedsearch;

public class TermFacetedSearchFormSettingsTest {
//    private static final TermFacetResult FACET_RESULT_WITH_THREE_TERMS = TermFacetResult.of(5L, 60L, 0L, asList(
//            TermStats.of("one", 30L),
//            TermStats.of("two", 20L),
//            TermStats.of("three", 10L)));
//    private static final List<String> SELECTED_VALUE_TWO = singletonList("two");
//    private static final List<FacetOption> OPTIONS = asList(
//            FacetOption.of("one", 30, false),
//            FacetOption.of("two", 20, true),
//            FacetOption.of("three", 10, false));
//    private static final TermFacetedSearchSearchModel<ProductProjection> SEARCH_MODEL = ProductProjectionSearchModel.of().facetedSearch().categories().id();
//
//    @Test
//    public void canBeDisplayedIfOverThreshold() throws Exception {
//        final SelectFacet<ProductProjection> facet = selectFacetWithThreeOptions().threshold(3L).build();
//        assertThat(facet.isAvailable()).isTrue();
//    }
//
//    @Test
//    public void canNotBeDisplayedIfBelowThreshold() throws Exception {
//        final SelectFacet<ProductProjection> facet = selectFacetWithThreeOptions().threshold(4L).build();
//        assertThat(facet.isAvailable()).isFalse();
//    }
//
//    @Test
//    public void optionsListIsTruncatedIfOverLimit() throws Exception {
//        final SelectFacet<ProductProjection> facet = selectFacetWithThreeOptions()
//                .selectedValues(SELECTED_VALUE_TWO)
//                .limit(2L)
//                .build();
//        assertThat(facet.getLimitedOptions()).containsExactlyElementsOf(asList(OPTIONS.get(0), OPTIONS.get(1)));
//        assertThat(facet.getAllOptions()).containsExactlyElementsOf(OPTIONS);
//    }
//
//    @Test
//    public void optionsListIsNotTruncatedIfBelowLimit() throws Exception {
//        final SelectFacet<ProductProjection> facet = selectFacetWithThreeOptions()
//                .selectedValues(SELECTED_VALUE_TWO)
//                .limit(3L)
//                .build();
//        assertThat(facet.getLimitedOptions()).containsExactlyElementsOf(OPTIONS);
//        assertThat(facet.getAllOptions()).containsExactlyElementsOf(OPTIONS);
//    }
//
//    @Test
//    public void filtersByAnyFilterValues() throws Exception {
//        final SelectFacet<ProductProjection> facet = selectFacet()
//                .selectedValues(SELECTED_VALUE_TWO)
//                .matchingAll(false)
//                .build();
//        final FacetedSearchExpression<ProductProjection> expr = facet.getFacetedSearchExpression();
//        final TermFacetedSearchExpression<ProductProjection> expectedExpr = SEARCH_MODEL.containsAny(SELECTED_VALUE_TWO);
//        assertThat(expr.facetExpression()).isEqualTo(expectedExpr.facetExpression());
//        assertThat(expr.filterExpressions()).isEqualTo(expectedExpr.filterExpressions());
//    }
//
//    @Test
//    public void filtersByAllFilterValues() throws Exception {
//        final SelectFacet<ProductProjection> facet = selectFacet()
//                .selectedValues(SELECTED_VALUE_TWO)
//                .matchingAll(true)
//                .build();
//        final FacetedSearchExpression<ProductProjection> expr = facet.getFacetedSearchExpression();
//        final TermFacetedSearchExpression<ProductProjection> expectedExpr = SEARCH_MODEL.containsAll(SELECTED_VALUE_TWO);
//        assertThat(expr.facetExpression()).isEqualTo(expectedExpr.facetExpression());
//        assertThat(expr.filterExpressions()).isEqualTo(expectedExpr.filterExpressions());
//    }
//
//    @Test
//    public void doesNotFilter() throws Exception {
//        final SelectFacet<ProductProjection> facet = selectFacet()
//                .matchingAll(true)
//                .build();
//        final FacetedSearchExpression<ProductProjection> expr = facet.getFacetedSearchExpression();
//        final TermFacetedSearchExpression<ProductProjection> expectedExpr = SEARCH_MODEL.allTerms();
//        assertThat(expr.facetExpression()).isEqualTo(expectedExpr.facetExpression());
//        assertThat(expr.filterExpressions()).isEqualTo(expectedExpr.filterExpressions());
//    }
//
//    private SelectFacetBuilder<ProductProjection> selectFacetWithThreeOptions() {
//        return selectFacet().facetResult(FACET_RESULT_WITH_THREE_TERMS);
//    }
//
//    private SelectFacetBuilder<ProductProjection> selectFacet() {
//        return SelectFacetBuilder.of("foo", SEARCH_MODEL);
//    }
}
