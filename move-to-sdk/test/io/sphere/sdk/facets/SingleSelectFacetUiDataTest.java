package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.TermStats;
import org.junit.Test;

import static io.sphere.sdk.facets.BaseSelectFacetUiDataTest.termUI;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class SingleSelectFacetUiDataTest {
    private static final String KEY = "single-select-facet";
    private static final String LABEL = "Select one option";
    private static final TermStats<String> TERM_ONE = TermStats.of("one", 30);
    private static final TermStats<String> TERM_TWO = TermStats.of("two", 20);
    private static final TermStats<String> TERM_THREE = TermStats.of("three", 10);
    private static final TermFacetResult<String> FACET_RESULT = TermFacetResult.of(5L, 60L, 0L, asList(TERM_ONE, TERM_TWO, TERM_THREE));
    private static final String SELECTED_VALUE = TERM_TWO.getTerm();

    @Test
    public void createsInstance() throws Exception {
        final SingleSelectFacetUiData<String> facetUI = SingleSelectFacetUiData.of(KEY, LABEL, FACET_RESULT, SELECTED_VALUE, 3L, 10L);
        assertThat(facetUI.getKey()).isEqualTo(KEY);
        assertThat(facetUI.getLabel()).isEqualTo(LABEL);
        assertThat(facetUI.getFacetResult()).isEqualTo(FACET_RESULT);
        assertThat(facetUI.getSelectedValue()).contains(SELECTED_VALUE);
        assertThat(facetUI.getTermsThreshold()).contains(3L);
        assertThat(facetUI.getTermsLimit()).contains(10L);
    }

    @Test
    public void createsInstanceWithoutOptionalValues() throws Exception {
        final SingleSelectFacetUiData<String> facetUI = SingleSelectFacetUiData.of(KEY, LABEL, FACET_RESULT);
        assertThat(facetUI.getKey()).isEqualTo(KEY);
        assertThat(facetUI.getLabel()).isEqualTo(LABEL);
        assertThat(facetUI.getFacetResult()).isEqualTo(FACET_RESULT);
        assertThat(facetUI.getSelectedValue()).isEmpty();
        assertThat(facetUI.getTermsLimit()).isEmpty();
        assertThat(facetUI.getTermsThreshold()).isEmpty();
    }

    @Test
    public void generatesTermsUI() throws Exception {
        final SingleSelectFacetUiData<String> facetUI = SingleSelectFacetUiData.of(KEY, LABEL, FACET_RESULT, SELECTED_VALUE, null, null);
        assertThat(facetUI.getAllTermsUI()).containsExactlyElementsOf(asList(termUI(TERM_ONE, false), termUI(TERM_TWO, true), termUI(TERM_THREE, false)));
    }
}
