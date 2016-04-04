package io.sphere.sdk.facets;

import org.junit.Test;

import java.util.List;

import static io.sphere.sdk.facets.CustomSortedFacetOptionMapper.comparePositions;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomSortedFacetOptionMapperTest {
    private static final FacetOption OPTION_A = FacetOption.of("A", 5, false);
    private static final FacetOption OPTION_B = FacetOption.of("B", 10, true);
    private static final FacetOption OPTION_C = FacetOption.of("C", 2, true);
    private static final FacetOption OPTION_D = FacetOption.of("D", 7, false);
    private static final FacetOption OPTION_E = FacetOption.of("E", 1, true);

    @Test
    public void sortsOptionsAsGivenList() throws Exception {
        final List<FacetOption> sortedFacetOptions = CustomSortedFacetOptionMapper.of(asList("A", "B", "C"))
                .apply(asList(OPTION_B, OPTION_A, OPTION_C));
        assertThat(sortedFacetOptions).containsExactly(OPTION_A, OPTION_B, OPTION_C);
    }

    @Test
    public void leavesUnknownOptionsAtTheEnd() throws Exception {
        final List<FacetOption> sortedFacetOptions = CustomSortedFacetOptionMapper.of(asList("A", "B", "C"))
                .apply(asList(OPTION_B, OPTION_D, OPTION_A, OPTION_C, OPTION_E));
        assertThat(sortedFacetOptions).containsExactly(OPTION_A, OPTION_B, OPTION_C, OPTION_D, OPTION_E);
    }

    @Test
    public void onEmptyListKeepsSameOrder() throws Exception {
        final List<FacetOption> sortedFacetOptions = CustomSortedFacetOptionMapper.of(emptyList())
                .apply(asList(OPTION_B, OPTION_D, OPTION_A, OPTION_C, OPTION_E));
        assertThat(sortedFacetOptions).containsExactly(OPTION_B, OPTION_D, OPTION_A, OPTION_C, OPTION_E);
    }

    @Test
    public void givenUnsortedFacetOptionsAreNotSorted() throws Exception {
        final List<FacetOption> unsortedFacetOptions = asList(OPTION_B, OPTION_A, OPTION_C);
        final List<FacetOption> sortedFacetOptions = CustomSortedFacetOptionMapper.of(asList("A", "B", "C"))
                .apply(unsortedFacetOptions);
        assertThat(unsortedFacetOptions).containsExactly(OPTION_B, OPTION_A, OPTION_C);
        assertThat(sortedFacetOptions).containsExactly(OPTION_A, OPTION_B, OPTION_C);
    }

    @Test
    public void worksWithEmptyFacetOptions() throws Exception {
        final List<FacetOption> sortedFacetOptions = CustomSortedFacetOptionMapper.of(asList("A", "B", "C"))
                .apply(emptyList());
        assertThat(sortedFacetOptions).isEmpty();
    }

    @Test
    public void comparisonEnsuresSymmetricRelation() throws Exception {
        assertThat(comparePositions(2, 3)).isEqualTo(-comparePositions(3, 2));
        assertThat(comparePositions(-1, 3)).isEqualTo(-comparePositions(3, -1));
        assertThat(comparePositions(3, -1)).isEqualTo(-comparePositions(-1, 3));
        assertThat(comparePositions(-1, -1)).isEqualTo(-comparePositions(-1, -1));
    }

    @Test
    public void comparisonEnsuresTransitiveRelation() throws Exception {
        assertThat(comparePositions(-1, 0)).isGreaterThan(0);
        assertThat(comparePositions(3, 0)).isGreaterThan(0);
        assertThat(comparePositions(-1, 3)).isGreaterThan(0);
    }
}
