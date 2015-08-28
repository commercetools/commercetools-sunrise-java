package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.TermStats;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class FacetOptionTest {
    private static final TermStats TERM_ONE = TermStats.of("one", 30);
    private static final TermStats TERM_TWO = TermStats.of("two", 20);
    private static final TermStats TERM_THREE = TermStats.of("three", 10);
    private static final TermFacetResult FACET_RESULT = TermFacetResult.of(5L, 60L, 0L, asList(TERM_ONE, TERM_TWO, TERM_THREE));

    @Test
    public void createsInstance() throws Exception {
        final FacetOption option = FacetOption.of("foo", 5, true);
        assertThat(option.getValue()).isEqualTo("foo");
        assertThat(option.getCount()).isEqualTo(5);
        assertThat(option.isSelected()).isTrue();
    }

    @Test
    public void createsInstanceFromTermStats() throws Exception {
        final FacetOption option = FacetOption.ofTermStats(TERM_TWO, asList(TERM_ONE.getTerm(), TERM_TWO.getTerm()));
        assertThat(option.getValue()).isEqualTo(TERM_TWO.getTerm());
        assertThat(option.getCount()).isEqualTo(TERM_TWO.getCount());
        assertThat(option.isSelected()).isTrue();
    }

    @Test
    public void createsInstanceFromTermStatsWithUnselectedValue() throws Exception {
        final FacetOption option = FacetOption.ofTermStats(TERM_THREE, asList(TERM_ONE.getTerm(), TERM_TWO.getTerm()));
        assertThat(option.getValue()).isEqualTo(TERM_THREE.getTerm());
        assertThat(option.getCount()).isEqualTo(TERM_THREE.getCount());
        assertThat(option.isSelected()).isFalse();
    }

    @Test
    public void createsInstanceFromFacetResult() throws Exception {
        final List<FacetOption> options = FacetOption.ofFacetResult(FACET_RESULT, asList(TERM_ONE.getTerm(), TERM_TWO.getTerm()));
        assertThat(options).containsExactlyElementsOf(asList(FacetOption.of("one", 30, true), FacetOption.of("two", 20, true), FacetOption.of("three", 10, false)));
    }

}
