package io.sphere.sdk.facets;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class SortedFacetOptionMapperTest {
    private static final FacetOption OPTION_A = FacetOption.of("A", 5, false);
    private static final FacetOption OPTION_B = FacetOption.of("B", 2, true);
    private static final FacetOption OPTION_C = FacetOption.of("C", 7, true);
    private static final FacetOption OPTION_D = FacetOption.of("D", 1, false);
    private static final FacetOption OPTION_E = FacetOption.of("E", 10, true);

    @Test
    public void sortsOptionsAsGivenList() throws Exception {
        final List<FacetOption> sortedFacetOptions = SortedFacetOptionMapper.of(asList("A", "B", "C"))
                .map(asList(OPTION_B, OPTION_A, OPTION_C));
        assertThat(sortedFacetOptions).containsExactly(OPTION_A, OPTION_B, OPTION_C);
    }

    @Test
    public void leavesUnknownOptionsAtTheEnd() throws Exception {
        final List<FacetOption> sortedFacetOptions = SortedFacetOptionMapper.of(asList("A", "B", "C"))
                .map(asList(OPTION_B, OPTION_D, OPTION_A, OPTION_C, OPTION_E));
        assertThat(sortedFacetOptions).containsExactly(OPTION_A, OPTION_B, OPTION_C, OPTION_D, OPTION_E);
    }

    @Test
    public void givenUnsortedFacetOptionsAreNotSorted() throws Exception {
        final List<FacetOption> unsortedFacetOptions = asList(OPTION_B, OPTION_A, OPTION_C);
        final List<FacetOption> sortedFacetOptions = SortedFacetOptionMapper.of(asList("A", "B", "C"))
                .map(unsortedFacetOptions);
        assertThat(unsortedFacetOptions).containsExactly(OPTION_B, OPTION_A, OPTION_C);
        assertThat(sortedFacetOptions).containsExactly(OPTION_A, OPTION_B, OPTION_C);
    }

    @Test
    public void worksWithEmptyFacetOptions() throws Exception {
        final List<FacetOption> sortedFacetOptions = SortedFacetOptionMapper.of(asList("A", "B", "C"))
                .map(emptyList());
        assertThat(sortedFacetOptions).isEmpty();
    }
}
