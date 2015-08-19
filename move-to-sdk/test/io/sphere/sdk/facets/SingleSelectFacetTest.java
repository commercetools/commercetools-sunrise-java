package io.sphere.sdk.facets;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class SingleSelectFacetTest {
    private static final String KEY = "single-select-facet";
    private static final String LABEL = "Select one option";
    private static final List<FacetOption<String>> OPTIONS = asList(
            FacetOption.of("one", 30, false),
            FacetOption.of("two", 20, true),
            FacetOption.of("three", 10, false));

    @Test
    public void createsInstance() throws Exception {
        final SingleSelectFacet<String> facet = SingleSelectFacetBuilder.of(KEY, LABEL, OPTIONS)
                .setThreshold(3L)
                .setLimit(10L)
                .build();
        assertThat(facet.getKey()).isEqualTo(KEY);
        assertThat(facet.getLabel()).isEqualTo(LABEL);
        assertThat(facet.getAllOptions()).isEqualTo(OPTIONS);
        assertThat(facet.getThreshold()).contains(3L);
        assertThat(facet.getLimit()).contains(10L);
    }

    @Test
    public void createsInstanceWithOptionalValues() throws Exception {
        final SingleSelectFacet<String> facet = SingleSelectFacetBuilder.of(KEY, LABEL, OPTIONS).build();
        assertThat(facet.getKey()).isEqualTo(KEY);
        assertThat(facet.getLabel()).isEqualTo(LABEL);
        assertThat(facet.getAllOptions()).isEqualTo(OPTIONS);
        assertThat(facet.getThreshold()).isEmpty();
        assertThat(facet.getLimit()).isEmpty();
    }
}
