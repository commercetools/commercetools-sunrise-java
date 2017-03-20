package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermStats;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class FacetOptionTest {
    private static final TermStats TERM_ONE = TermStats.of("one", 0L, 30L);
    private static final TermStats TERM_TWO = TermStats.of("two", 0L, 20L);
    private static final TermStats TERM_THREE = TermStats.of("three", 0L, 10L);

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
        assertThat(option.getCount()).isEqualTo(TERM_TWO.getProductCount());
        assertThat(option.isSelected()).isTrue();
    }

    @Test
    public void createsInstanceFromTermStatsWithUnselectedValue() throws Exception {
        final FacetOption option = FacetOption.ofTermStats(TERM_THREE, asList(TERM_ONE.getTerm(), TERM_TWO.getTerm()));
        assertThat(option.getValue()).isEqualTo(TERM_THREE.getTerm());
        assertThat(option.getCount()).isEqualTo(TERM_THREE.getProductCount());
        assertThat(option.isSelected()).isFalse();
    }

    @Test
    public void createsInstanceWithDifferentValue() throws Exception {
        final FacetOption option = FacetOption.ofTermStats(TERM_ONE, emptyList());
        assertThat(option.getValue()).isNotEqualTo("foo");
        assertThat(option.withValue("foo").getValue()).isEqualTo("foo");
    }

    @Test
    public void createsInstanceWithDifferentCount() throws Exception {
        final FacetOption option = FacetOption.ofTermStats(TERM_ONE, emptyList());
        assertThat(option.getCount()).isNotEqualTo(100);
        assertThat(option.withCount(100).getCount()).isEqualTo(100);
    }

    @Test
    public void createsInstanceWithDifferentSelected() throws Exception {
        final FacetOption option = FacetOption.ofTermStats(TERM_ONE, emptyList());
        assertThat(option.isSelected()).isFalse();
        assertThat(option.withSelected(true).isSelected()).isTrue();
    }

    @Test
    public void createsInstanceWithDifferentChildren() throws Exception {
        final List<FacetOption> childrenOptions = asList(
                FacetOption.of("foo", 2, true),
                FacetOption.of("bar", 6, false).withChildren(singletonList(FacetOption.of("bar2", 4, true))));

        final FacetOption option = FacetOption.ofTermStats(TERM_ONE, emptyList());
        assertThat(option.getChildren()).isEmpty();
        assertThat(option.withChildren(childrenOptions).getChildren()).containsExactlyElementsOf(childrenOptions);
    }

}
