package io.sphere.sdk.facets;

import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearchModel;
import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.TermModel;
import io.sphere.sdk.search.TermStats;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BaseSelectFacetTest {
    private static final TermStats TERM_ONE = TermStats.of("one", 30);
    private static final TermStats TERM_TWO = TermStats.of("two", 20);
    private static final TermStats TERM_THREE = TermStats.of("three", 10);
    private static final TermFacetResult FACET_RESULT = TermFacetResult.of(5L, 60L, 0L, asList(TERM_ONE, TERM_TWO, TERM_THREE));
    private static final String KEY = "single-select-facet";
    private static final String LABEL = "Select one option";
    private static final TermModel<ProductProjection, ?> SEARCH_MODEL = ProductProjectionSearchModel.of().categories().id();
    private static final List<FacetOption> OPTIONS = asList(
            FacetOption.of("one", 30, false),
            FacetOption.of("two", 20, true),
            FacetOption.of("three", 10, false));

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
        final SelectFacet<ProductProjection> facet = selectFacetWithThreeOptions().limit(2L).build();
        assertThat(facet.getLimitedOptions()).containsExactlyElementsOf(asList(OPTIONS.get(0), OPTIONS.get(1)));
        assertThat(facet.getAllOptions()).containsExactlyElementsOf(OPTIONS);
    }

    @Test
    public void optionsListIsNotTruncatedIfBelowLimit() throws Exception {
        final SelectFacet<ProductProjection> facet = selectFacetWithThreeOptions().limit(3L).build();
        assertThat(facet.getLimitedOptions()).containsExactlyElementsOf(OPTIONS);
        assertThat(facet.getAllOptions()).containsExactlyElementsOf(OPTIONS);
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
        return SelectFacetBuilder.of(KEY, LABEL, SEARCH_MODEL)
                .termFacetResult(FACET_RESULT)
                .selectedValues(singletonList("two"));
    }
}
