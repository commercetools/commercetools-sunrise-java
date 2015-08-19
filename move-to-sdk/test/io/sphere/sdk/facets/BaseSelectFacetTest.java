package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermStats;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BaseSelectFacetTest {
    private static final String KEY = "single-select-facet";
    private static final String LABEL = "Select one option";
    private static final List<FacetOption<String>> OPTIONS = asList(
            FacetOption.of("one", 30, false),
            FacetOption.of("two", 20, true),
            FacetOption.of("three", 10, false));

    @Test
    public void canBeDisplayedIfOverThreshold() throws Exception {
        final SingleSelectFacet<String> facet = SingleSelectFacetBuilder.of(KEY, LABEL, OPTIONS)
                .threshold(3L)
                .build();
        assertThat(facet.canBeDisplayed()).isTrue();
    }

    @Test
    public void canNotBeDisplayedIfBelowThreshold() throws Exception {
        final SingleSelectFacet<String> facet = SingleSelectFacetBuilder.of(KEY, LABEL, OPTIONS)
                .threshold(4L)
                .build();
        assertThat(facet.canBeDisplayed()).isFalse();
    }

    @Test
    public void termListIsTruncatedIfOverLimit() throws Exception {
        final SingleSelectFacet<String> facet = SingleSelectFacetBuilder.of(KEY, LABEL, OPTIONS)
                .limit(2L)
                .build();
        assertThat(facet.getLimitedOptions()).containsExactlyElementsOf(asList(OPTIONS.get(0), OPTIONS.get(1)));
        assertThat(facet.getAllOptions()).containsExactlyElementsOf(OPTIONS);
    }

    @Test
    public void termListIsNotTruncatedIfBelowLimit() throws Exception {
        final SingleSelectFacet<String> facet = SingleSelectFacetBuilder.of(KEY, LABEL, OPTIONS)
                .limit(3L)
                .build();
        assertThat(facet.getLimitedOptions()).containsExactlyElementsOf(OPTIONS);
        assertThat(facet.getAllOptions()).containsExactlyElementsOf(OPTIONS);
    }

    @Test
    public void throwsExceptionOnWrongThresholdAndLimit() throws Exception {
        final SingleSelectFacetBuilder<String> builder = SingleSelectFacetBuilder.of(KEY, LABEL, OPTIONS).threshold(10L);
        assertThatThrownBy(() -> {
            builder.limit(10L).build();
            builder.limit(3L).build();
        }).isExactlyInstanceOf(InvalidSelectFacetConstraintsException.class)
                .hasMessageContaining("Threshold: 10")
                .hasMessageContaining("Limit: 3");
    }

    static <T> FacetOption<T> termUI(final TermStats<T> termStats, final boolean selected) {
        return new FacetOption<>(termStats.getTerm(), termStats.getCount(), selected);
    }
}
