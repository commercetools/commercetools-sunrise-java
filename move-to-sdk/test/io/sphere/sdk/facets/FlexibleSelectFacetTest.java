package io.sphere.sdk.facets;

import com.fasterxml.jackson.databind.JsonNode;
import io.sphere.sdk.json.SphereJsonUtils;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearchModel;
import io.sphere.sdk.search.PagedSearchResult;
import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.TermModel;
import io.sphere.sdk.search.TermStats;
import org.junit.Test;
import play.libs.Json;

import java.util.List;

import static io.sphere.sdk.facets.DefaultFacetType.SORTED_SELECT;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class FlexibleSelectFacetTest {
    private static final String KEY = "single-select-facet";
    private static final String LABEL = "Select one option";
    private static final TermModel<ProductProjection, ?> SEARCH_MODEL = ProductProjectionSearchModel.of().categories().id();
    private static final TermFacetResult FACET_RESULT_WITH_THREE_TERMS = TermFacetResult.of(5L, 60L, 0L, asList(
            TermStats.of("one", 30),
            TermStats.of("two", 20),
            TermStats.of("three", 10)));
    private static final List<String> SELECTED_VALUE_TWO = singletonList("two");
    private static final List<FacetOption> OPTIONS = asList(
            FacetOption.of("one", 30, false),
            FacetOption.of("two", 20, true),
            FacetOption.of("three", 10, false));
    public static final FacetOptionMapper MAPPER = list -> list;

    @Test
    public void createsInstance() throws Exception {
        final FlexibleSelectFacet<ProductProjection> facet = FlexibleSelectFacetBuilder.of(KEY, LABEL, SORTED_SELECT, SEARCH_MODEL, MAPPER)
                .facetResult(FACET_RESULT_WITH_THREE_TERMS)
                .selectedValues(SELECTED_VALUE_TWO)
                .matchingAll(true)
                .multiSelect(false)
                .threshold(4L)
                .limit(10L)
                .build();
        assertThat(facet.getKey()).isEqualTo(KEY);
        assertThat(facet.getLabel()).isEqualTo(LABEL);
        assertThat(facet.getType()).isEqualTo(SORTED_SELECT);
        assertThat(facet.getSearchModel()).isEqualTo(SEARCH_MODEL.untyped());
        assertThat(facet.getMapper()).isEqualTo(MAPPER);
        assertThat(facet.getFacetResult()).contains(FACET_RESULT_WITH_THREE_TERMS);
        assertThat(facet.getSelectedValues()).containsExactlyElementsOf(SELECTED_VALUE_TWO);
        assertThat(facet.isMatchingAll()).isTrue();
        assertThat(facet.isMultiSelect()).isFalse();
        assertThat(facet.getThreshold()).contains(4L);
        assertThat(facet.getLimit()).contains(10L);
        assertThat(facet.isAvailable()).isFalse();
        assertThat(facet.getAllOptions()).containsExactlyElementsOf(OPTIONS);
        assertThat(facet.getLimitedOptions()).containsExactlyElementsOf(OPTIONS);
    }

    @Test
    public void createsInstanceWithOptionalValues() throws Exception {
        final FlexibleSelectFacet<ProductProjection> facet = FlexibleSelectFacetBuilder.of(KEY, LABEL, SORTED_SELECT, SEARCH_MODEL, MAPPER).build();
        assertThat(facet.getKey()).isEqualTo(KEY);
        assertThat(facet.getLabel()).isEqualTo(LABEL);
        assertThat(facet.getType()).isEqualTo(SORTED_SELECT);
        assertThat(facet.getSearchModel()).isEqualTo(SEARCH_MODEL.untyped());
        assertThat(facet.getMapper()).isEqualTo(MAPPER);
        assertThat(facet.getFacetResult()).isEmpty();
        assertThat(facet.getSelectedValues()).isEmpty();
        assertThat(facet.isMatchingAll()).isFalse();
        assertThat(facet.isMultiSelect()).isTrue();
        assertThat(facet.getThreshold()).isEmpty();
        assertThat(facet.getLimit()).isEmpty();
        assertThat(facet.isAvailable()).isTrue();
        assertThat(facet.getAllOptions()).isEmpty();
        assertThat(facet.getLimitedOptions()).isEmpty();
    }

    @Test
    public void mapsOptions() throws Exception {
        final SortedFacetOptionMapper mapper = SortedFacetOptionMapper.of(asList("two", "three", "one"));
        final FlexibleSelectFacet<ProductProjection> facet = FlexibleSelectFacetBuilder.of(KEY, LABEL, SORTED_SELECT, SEARCH_MODEL, mapper)
                .facetResult(FACET_RESULT_WITH_THREE_TERMS)
                .selectedValues(SELECTED_VALUE_TWO)
                .build();
        assertThat(facet.getMapper()).isEqualTo(mapper);
        assertThat(facet.getAllOptions()).containsExactly(OPTIONS.get(1), OPTIONS.get(2), OPTIONS.get(0));
        assertThat(facet.getLimitedOptions()).containsExactly(OPTIONS.get(1), OPTIONS.get(2), OPTIONS.get(0));
    }

    @Test
    public void createsInstanceWithDifferentSelectedValues() throws Exception {
        final FlexibleSelectFacet<ProductProjection> facet = FlexibleSelectFacetBuilder.of(KEY, LABEL, SORTED_SELECT, SEARCH_MODEL, MAPPER).build();
        assertThat(facet.getSelectedValues()).isEmpty();
        assertThat(facet.withSelectedValues(SELECTED_VALUE_TWO).getSelectedValues()).containsExactlyElementsOf(SELECTED_VALUE_TWO);
    }

    @Test
    public void createsInstanceWithDifferentFacetResult() throws Exception {
        final FlexibleSelectFacet<ProductProjection> facet = FlexibleSelectFacetBuilder.of(KEY, LABEL, SORTED_SELECT, SEARCH_MODEL, MAPPER).build();
        assertThat(facet.getFacetResult()).isEmpty();
        assertThat(facet.withSearchResult(searchResult()).getFacetResult()).contains(FACET_RESULT_WITH_THREE_TERMS);
    }

    @SuppressWarnings("unchecked")
    private PagedSearchResult<ProductProjection> searchResult() {
        final String json = "{" +
                        "  \"facets\": {" +
                        "    \"categories.id\": {" +
                        "      \"type\": \"terms\"," +
                        "      \"missing\": 5," +
                        "      \"total\": 60," +
                        "      \"other\": 0," +
                        "      \"terms\": [" +
                        "        { " +
                        "          \"term\": \"one\", " +
                        "          \"count\": 30 " +
                        "        }," +
                        "        { " +
                        "          \"term\": \"two\", " +
                        "          \"count\": 20 " +
                        "        }," +
                        "        { " +
                        "          \"term\": \"three\", " +
                        "          \"count\": 10 " +
                        "        }" +
                        "      ]" +
                        "    }" +
                        "  }" +
                        "}";
        final JsonNode node = Json.parse(json);
        return SphereJsonUtils.readObject(node, PagedSearchResult.class);
    }
}
