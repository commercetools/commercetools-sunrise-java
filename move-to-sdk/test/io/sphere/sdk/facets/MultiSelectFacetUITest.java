package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.TermStats;
import org.junit.Test;

import java.util.List;

import static io.sphere.sdk.facets.BaseSelectFacetUITest.termUI;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class MultiSelectFacetUITest {
    private static final String KEY = "multi-select-facet";
    private static final String LABEL = "Select any options";
    private static final TermStats<String> TERM_ONE = TermStats.of("one", 30);
    private static final TermStats<String> TERM_TWO = TermStats.of("two", 20);
    private static final TermStats<String> TERM_THREE = TermStats.of("three", 10);
    private static final TermFacetResult<String> FACET_RESULT = TermFacetResult.of(5L, 60L, 0L, asList(TERM_ONE, TERM_TWO, TERM_THREE));
    private static final List<String> SELECTED_VALUES = asList(TERM_TWO.getTerm(), TERM_THREE.getTerm());

    @Test
    public void createsInstance() throws Exception {
        final MultiSelectFacetUI<String> facetUI = MultiSelectFacetUI.of(KEY, LABEL, FACET_RESULT, SELECTED_VALUES, 3L, 10L, true);
        assertThat(facetUI.getKey()).isEqualTo(KEY);
        assertThat(facetUI.getLabel()).isEqualTo(LABEL);
        assertThat(facetUI.getFacetResult()).isEqualTo(FACET_RESULT);
        assertThat(facetUI.getSelectedValues()).isEqualTo(SELECTED_VALUES);
        assertThat(facetUI.getTermsThreshold()).contains(3L);
        assertThat(facetUI.getTermsLimit()).contains(10L);
        assertThat(facetUI.matchesAll()).isTrue();
    }

    @Test
    public void createsInstanceWithoutOptionalValues() throws Exception {
        final MultiSelectFacetUI<String> facetUI = MultiSelectFacetUI.of(KEY, LABEL, FACET_RESULT, false);
        assertThat(facetUI.getKey()).isEqualTo(KEY);
        assertThat(facetUI.getLabel()).isEqualTo(LABEL);
        assertThat(facetUI.getFacetResult()).isEqualTo(FACET_RESULT);
        assertThat(facetUI.getSelectedValues()).isEmpty();
        assertThat(facetUI.getTermsLimit()).isEmpty();
        assertThat(facetUI.getTermsThreshold()).isEmpty();
        assertThat(facetUI.matchesAll()).isFalse();
    }

    @Test
    public void generatesTermsUI() throws Exception {
        final MultiSelectFacetUI<String> facetUI = MultiSelectFacetUI.of(KEY, LABEL, FACET_RESULT, SELECTED_VALUES, null, null, true);
        assertThat(facetUI.getAllTermsUI()).containsExactlyElementsOf(asList(termUI(TERM_ONE, false), termUI(TERM_TWO, true), termUI(TERM_THREE, true)));
    }
}
