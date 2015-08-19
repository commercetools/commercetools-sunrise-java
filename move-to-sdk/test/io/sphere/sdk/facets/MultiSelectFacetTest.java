package io.sphere.sdk.facets;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class MultiSelectFacetTest {
    private static final String KEY = "multi-select-facet";
    private static final String LABEL = "Select options";
    private static final List<FacetOption<String>> OPTIONS = asList(
            FacetOption.of("one", 30, true),
            FacetOption.of("two", 20, true),
            FacetOption.of("three", 10, false));

    @Test
    public void createsInstance() throws Exception {
        final MultiSelectFacet<String> facet = MultiSelectFacetBuilder.of(KEY, LABEL, OPTIONS, true)
                .threshold(3L)
                .limit(10L)
                .build();
        assertThat(facet.getKey()).isEqualTo(KEY);
        assertThat(facet.getLabel()).isEqualTo(LABEL);
        assertThat(facet.getAllOptions()).isEqualTo(OPTIONS);
        assertThat(facet.matchesAll()).isTrue();
        assertThat(facet.getThreshold()).contains(3L);
        assertThat(facet.getLimit()).contains(10L);
    }

    @Test
    public void createsInstanceWithOptionalValues() throws Exception {
        final MultiSelectFacet<String> facet = MultiSelectFacetBuilder.of(KEY, LABEL, OPTIONS, false).build();
        assertThat(facet.getKey()).isEqualTo(KEY);
        assertThat(facet.getLabel()).isEqualTo(LABEL);
        assertThat(facet.getAllOptions()).isEqualTo(OPTIONS);
        assertThat(facet.matchesAll()).isFalse();
        assertThat(facet.getThreshold()).isEmpty();
        assertThat(facet.getLimit()).isEmpty();
    }
}
