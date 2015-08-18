package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.TermStats;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BaseSelectFacetUITest {
    private static final String KEY = "select-facet";
    private static final String LABEL = "Select";
    private static final TermStats<String> TERM_ONE = TermStats.of("one", 30);
    private static final TermStats<String> TERM_TWO = TermStats.of("two", 20);
    private static final TermStats<String> TERM_THREE = TermStats.of("three", 10);
    private static final TermFacetResult<String> FACET_RESULT = TermFacetResult.of(5L, 60L, 0L, asList(TERM_ONE, TERM_TWO, TERM_THREE));
    private static final String SELECTED_VALUE = TERM_TWO.getTerm();

    @Test
    public void canBeDisplayedIfOverThreshold() throws Exception {
        final SingleSelectFacetUI<String> facetUI = SingleSelectFacetUI.of(KEY, LABEL, FACET_RESULT, SELECTED_VALUE, 3L, null);
        assertThat(facetUI.canBeDisplayed()).isTrue();
    }

    @Test
    public void canNotBeDisplayedIfBelowThreshold() throws Exception {
        final SingleSelectFacetUI<String> facetUI = SingleSelectFacetUI.of(KEY, LABEL, FACET_RESULT, SELECTED_VALUE, 4L, null);
        assertThat(facetUI.canBeDisplayed()).isFalse();
    }

    @Test
    public void termListIsTruncatedIfOverLimit() throws Exception {
        final SingleSelectFacetUI<String> facetUI = SingleSelectFacetUI.of(KEY, LABEL, FACET_RESULT, SELECTED_VALUE, null, 2L);
        assertThat(facetUI.getLimitedTermsUI()).containsExactlyElementsOf(asList(termUI(TERM_ONE, false), termUI(TERM_TWO, true)));
    }

    @Test
    public void termListIsNotTruncatedIfBelowLimit() throws Exception {
        final SingleSelectFacetUI<String> facetUI = SingleSelectFacetUI.of(KEY, LABEL, FACET_RESULT, SELECTED_VALUE, null, 3L);
        assertThat(facetUI.getLimitedTermsUI()).containsExactlyElementsOf(facetUI.getAllTermsUI());
    }

    @Test
    public void throwsExceptionOnWrongThresholdAndLimit() throws Exception {
        assertThatThrownBy(() -> {
            SingleSelectFacetUI.of(KEY, LABEL, FACET_RESULT, SELECTED_VALUE, 5L, 5L);
            SingleSelectFacetUI.of(KEY, LABEL, FACET_RESULT, SELECTED_VALUE, 10L, 3L);
        }).isExactlyInstanceOf(InvalidSelectFacetConstraintsException.class)
                .hasMessageContaining("Threshold: 10")
                .hasMessageContaining("Limit: 3");
    }

    static <T> TermUI<T> termUI(final TermStats<T> termStats, final boolean selected) {
        return new TermUI<>(termStats.getTerm(), termStats.getCount(), selected);
    }
}
