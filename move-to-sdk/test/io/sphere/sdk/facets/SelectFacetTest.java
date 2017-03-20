package io.sphere.sdk.facets;

import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearchModel;
import io.sphere.sdk.search.FacetedSearchExpression;
import io.sphere.sdk.search.SearchExpression;
import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.TermStats;
import io.sphere.sdk.search.model.TermFacetedSearchSearchModel;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SelectFacetTest {
    private static final TermFacetResult FACET_RESULT_WITH_THREE_TERMS = TermFacetResult.of(5L, 60L, 0L, asList(
            TermStats.of("one", 60L, 30L),
            TermStats.of("two", 40L, 20L),
            TermStats.of("three", 20L, 10L)));
    private static final List<String> SELECTED_VALUES = asList("two", "three");
    private static final List<FacetOption> OPTIONS = asList(
            FacetOption.of("one", 30, false),
            FacetOption.of("two", 20, true),
            FacetOption.of("three", 10, true));
    private static final TermFacetedSearchSearchModel<ProductProjection> SEARCH_MODEL = ProductProjectionSearchModel.of().facetedSearch().categories().id();

    @Test
    public void canBeDisplayedIfOverThreshold() throws Exception {
        final SelectFacet<ProductProjection> facet = selectFacetWithThreeOptions().threshold(3L).build();
        assertThat(facet.isAvailable()).isTrue();
    }

    @Test
    public void canNotBeDisplayedIfBelowThreshold() throws Exception {
        final SelectFacet<ProductProjection> facet = selectFacetWithThreeOptions().threshold(4L).build();
        assertThat(facet.isAvailable()).isFalse();
    }

    @Test
    public void optionsListIsTruncatedIfOverLimit() throws Exception {
        final SelectFacet<ProductProjection> facet = selectFacetWithThreeOptions()
                .selectedValues(SELECTED_VALUES)
                .limit(2L)
                .build();
        assertThat(facet.getLimitedOptions()).containsExactlyElementsOf(asList(OPTIONS.get(0), OPTIONS.get(1)));
        assertThat(facet.getAllOptions()).containsExactlyElementsOf(OPTIONS);
    }

    @Test
    public void optionsListIsNotTruncatedIfBelowLimit() throws Exception {
        final SelectFacet<ProductProjection> facet = selectFacetWithThreeOptions()
                .selectedValues(SELECTED_VALUES)
                .limit(3L)
                .build();
        assertThat(facet.getLimitedOptions()).containsExactlyElementsOf(OPTIONS);
        assertThat(facet.getAllOptions()).containsExactlyElementsOf(OPTIONS);
    }

    @Test
    public void filtersByAnyFilterValues() throws Exception {
        final SelectFacet<ProductProjection> facet = selectFacet()
                .selectedValues(SELECTED_VALUES)
                .matchingAll(false)
                .build();
        final FacetedSearchExpression<ProductProjection> expr = facet.getFacetedSearchExpression();
        assertThat(expr.facetExpression().expression()).isEqualTo("categories.id counting products");
        assertThat(expr.filterExpressions())
                .extracting(SearchExpression::expression)
                .containsExactly("categories.id:\"two\",\"three\"");
    }

    @Test
    public void filtersByAllFilterValues() throws Exception {
        final SelectFacet<ProductProjection> facet = selectFacet()
                .selectedValues(SELECTED_VALUES)
                .matchingAll(true)
                .build();
        final FacetedSearchExpression<ProductProjection> expr = facet.getFacetedSearchExpression();
        assertThat(expr.facetExpression().expression()).isEqualTo("categories.id counting products");
        assertThat(expr.filterExpressions())
                .extracting(SearchExpression::expression)
                .containsExactly("categories.id:\"two\"", "categories.id:\"three\"");
    }

    @Test
    public void doesNotFilter() throws Exception {
        final SelectFacet<ProductProjection> facet = selectFacet()
                .matchingAll(true)
                .build();
        final FacetedSearchExpression<ProductProjection> expr = facet.getFacetedSearchExpression();
        assertThat(expr.facetExpression().expression()).isEqualTo("categories.id counting products");
        assertThat(expr.filterExpressions()).isEmpty();
    }

    @Test
    public void throwsExceptionOnWrongThresholdAndLimit() throws Exception {
        final SelectFacetBuilder<ProductProjection> builder = selectFacetWithThreeOptions().threshold(10L);
        assertThatThrownBy(() -> {
            builder.limit(10L).build();
            builder.limit(3L).build();
        }).isExactlyInstanceOf(InvalidSelectFacetConstraintsException.class)
                .hasMessageContaining("Threshold: 10")
                .hasMessageContaining("Limit: 3");
    }

    private SelectFacetBuilder<ProductProjection> selectFacetWithThreeOptions() {
        return selectFacet().facetResult(FACET_RESULT_WITH_THREE_TERMS);
    }

    private SelectFacetBuilder<ProductProjection> selectFacet() {
        return SelectFacetBuilder.of("foo", SEARCH_MODEL);
    }
}
