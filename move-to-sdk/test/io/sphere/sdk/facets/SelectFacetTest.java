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

public class SelectFacetTest {
    private static final TermStats TERM_ONE = TermStats.of("one", 30);
    private static final TermStats TERM_TWO = TermStats.of("two", 20);
    private static final TermStats TERM_THREE = TermStats.of("three", 10);
    private static final TermFacetResult FACET_RESULT = TermFacetResult.of(5L, 60L, 0L, asList(TERM_ONE, TERM_TWO, TERM_THREE));

    private static final TermModel<ProductProjection, ?> SEARCH_MODEL = ProductProjectionSearchModel.of().categories().id();
    private static final String KEY = "single-select-facet";
    private static final String LABEL = "Select one option";
    private static final List<FacetOption> OPTIONS = asList(
            FacetOption.of("one", 30, false),
            FacetOption.of("two", 20, true),
            FacetOption.of("three", 10, false));

    @Test
    public void createsInstance() throws Exception {
        final SelectFacet<ProductProjection> facet = SelectFacetBuilder.of(KEY, LABEL, SEARCH_MODEL)
                .termFacetResult(FACET_RESULT)
                .selectedValues(singletonList("two"))
                .threshold(3L)
                .limit(10L)
                .build();
        assertThat(facet.getKey()).isEqualTo(KEY);
        assertThat(facet.getLabel()).isEqualTo(LABEL);
        assertThat(facet.getAllOptions()).isEqualTo(OPTIONS);
        assertThat(facet.getThreshold()).contains(3L);
        assertThat(facet.getLimit()).contains(10L);
    }

    @Test
    public void createsInstanceWithOptionalValues() throws Exception {
        final SelectFacet<ProductProjection> facet = SelectFacetBuilder.of(KEY, LABEL, SEARCH_MODEL).build();
        assertThat(facet.getKey()).isEqualTo(KEY);
        assertThat(facet.getLabel()).isEqualTo(LABEL);
        assertThat(facet.getAllOptions()).isEmpty();
        assertThat(facet.getThreshold()).isEmpty();
        assertThat(facet.getLimit()).isEmpty();
    }
}
