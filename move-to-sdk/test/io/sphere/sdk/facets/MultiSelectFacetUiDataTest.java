package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.TermStats;
import org.junit.Test;

import java.util.List;

import static io.sphere.sdk.facets.BaseSelectFacetUiDataTest.termUI;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class MultiSelectFacetUiDataTest {
    private static final String KEY = "multi-select-facet";
    private static final String LABEL = "Select any options";
    private static final TermStats<String> TERM_ONE = TermStats.of("one", 30);
    private static final TermStats<String> TERM_TWO = TermStats.of("two", 20);
    private static final TermStats<String> TERM_THREE = TermStats.of("three", 10);
    private static final TermFacetResult<String> FACET_RESULT = TermFacetResult.of(5L, 60L, 0L, asList(TERM_ONE, TERM_TWO, TERM_THREE));
    private static final List<String> SELECTED_VALUES = asList(TERM_TWO.getTerm(), TERM_THREE.getTerm());

    @Test
    public void createsInstance() throws Exception {
        final MultiSelectFacetUiData<String> facet = MultiSelectFacetUiData.of(KEY, LABEL, FACET_RESULT, SELECTED_VALUES, 3L, 10L, true);
        assertThat(facet.getKey()).isEqualTo(KEY);
        assertThat(facet.getLabel()).isEqualTo(LABEL);
        assertThat(facet.getFacetResult()).isEqualTo(FACET_RESULT);
        assertThat(facet.getSelectedValues()).isEqualTo(SELECTED_VALUES);
        assertThat(facet.getThreshold()).contains(3L);
        assertThat(facet.getLimit()).contains(10L);
        assertThat(facet.matchesAll()).isTrue();
    }

    @Test
    public void createsInstanceWithoutOptionalValues() throws Exception {
        final MultiSelectFacetUiData<String> facet = MultiSelectFacetUiData.of(KEY, LABEL, FACET_RESULT, false);
        assertThat(facet.getKey()).isEqualTo(KEY);
        assertThat(facet.getLabel()).isEqualTo(LABEL);
        assertThat(facet.getFacetResult()).isEqualTo(FACET_RESULT);
        assertThat(facet.getSelectedValues()).isEmpty();
        assertThat(facet.getLimit()).isEmpty();
        assertThat(facet.getThreshold()).isEmpty();
        assertThat(facet.matchesAll()).isFalse();
    }

    @Test
    public void generatesTermsUI() throws Exception {
        final MultiSelectFacetUiData<String> facet = MultiSelectFacetUiData.of(KEY, LABEL, FACET_RESULT, SELECTED_VALUES, null, null, true);
        assertThat(facet.getAllTermsUiData()).containsExactlyElementsOf(asList(termUI(TERM_ONE, false), termUI(TERM_TWO, true), termUI(TERM_THREE, true)));
    }
}
