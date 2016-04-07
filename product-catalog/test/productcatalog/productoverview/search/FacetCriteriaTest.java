package productcatalog.productoverview.search;

import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.facets.AlphabeticallySortedFacetOptionMapper;
import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.facets.SelectFacet;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.FilterExpression;
import io.sphere.sdk.search.PagedSearchResult;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

import static io.sphere.sdk.json.SphereJsonUtils.readObjectFromResource;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Locale.ENGLISH;
import static org.assertj.core.api.Assertions.assertThat;

public class FacetCriteriaTest {

    private static final String KEY = "key";
    private static final String ATTR_PATH = "foo.bar";
    private static final String LABEL = "Some Facet";

    @Test
    public void initializesFacet() throws Exception {
        final FacetMapperConfig mapperConfig = FacetMapperConfig.of("alphabeticallySortedFacet", emptyList());
        final FacetConfig config = FacetConfig.of(SunriseFacetType.SELECT_LIST_DISPLAY, KEY, LABEL, ATTR_PATH, true, true, false, 3L, 2L, mapperConfig);
        final List<String> selectedValues = asList("foo", "bar");
        test(config, selectedValues, ENGLISH, facetCriteria -> {
            final Facet<ProductProjection> facet = facetCriteria.getFacet(searchResult());
            assertThat(facet).as("class").isInstanceOf(SelectFacet.class);
            final SelectFacet<ProductProjection> selectFacet = (SelectFacet<ProductProjection>) facet;
            assertThat(selectFacet.getType()).as("type").isEqualTo(SunriseFacetType.SELECT_LIST_DISPLAY);
            assertThat(selectFacet.getKey()).as("key").isEqualTo(KEY);
            assertThat(selectFacet.getLabel()).as("label").contains(LABEL);
            assertThat(selectFacet.isCountHidden()).as("count hidden").isTrue();
            assertThat(selectFacet.isMatchingAll()).as("matching all").isTrue();
            assertThat(selectFacet.isMultiSelect()).as("multi select").isFalse();
            assertThat(selectFacet.getLimit()).as("limit").contains(3L);
            assertThat(selectFacet.getThreshold()).as("threshold").contains(2L);
            assertThat(selectFacet.getMapper().get()).as("mapper").isInstanceOf(AlphabeticallySortedFacetOptionMapper.class);
            assertThat(selectFacet.getSelectedValues()).as("selected values").containsExactlyElementsOf(selectedValues);
        });
    }

    @Test
    public void getsFacetedSearchExprWithSelectedValueMatchingAll() throws Exception {
        final FacetConfig config = facetConfig("attr", true);
        final List<String> selectedValues = asList("foo", "bar");
        test(config, selectedValues, ENGLISH, facetCriteria -> {
            Assertions.assertThat(facetCriteria.getFacetedSearchExpression().facetExpression().expression()).isEqualTo("attr");
            Assertions.assertThat(facetCriteria.getFacetedSearchExpression().filterExpressions()).extracting(FilterExpression::expression).containsOnly("attr:\"foo\"", "attr:\"bar\"");
        });
    }

    @Test
    public void getsFacetedSearchExprWithSelectedValueMatchingAny() throws Exception {
        final FacetConfig config = facetConfig("attr", false);
        final List<String> selectedValues = asList("foo", "bar");
        test(config, selectedValues, ENGLISH, facetCriteria -> {
            Assertions.assertThat(facetCriteria.getFacetedSearchExpression().facetExpression().expression()).isEqualTo("attr");
            Assertions.assertThat(facetCriteria.getFacetedSearchExpression().filterExpressions()).extracting(FilterExpression::expression).containsOnly("attr:\"foo\",\"bar\"");
        });
    }

    @Test
    public void getsFacetedSearchExprWithLocale() throws Exception {
        final FacetConfig config = facetConfig("some.{{locale}}.foo.{{locale}}.bar", true);
        final List<String> selectedValues = emptyList();
        test(config, selectedValues, ENGLISH, facetCriteria -> {
            Assertions.assertThat(facetCriteria.getFacetedSearchExpression().facetExpression().expression()).isEqualTo("some.en.foo.en.bar");
            Assertions.assertThat(facetCriteria.getFacetedSearchExpression().filterExpressions()).isEmpty();
        });
    }

    private FacetConfig facetConfig(final String attrPath, final boolean matchingAll) {
        return FacetConfig.of(SunriseFacetType.SELECT_LIST_DISPLAY, KEY, LABEL, attrPath, true, matchingAll, false);
    }

    private void test(final FacetConfig config, final List<String> selectedValues, final Locale locale, final Consumer<FacetCriteria> test) {
        final Map<String, List<String>> queryString = Collections.singletonMap(KEY, selectedValues);
        final FacetCriteria criteria = FacetCriteria.of(config, queryString, singletonList(locale), emptyList(), CategoryTree.of(emptyList()));
        test.accept(criteria);
    }

    private static PagedSearchResult<ProductProjection> searchResult() {
        return readObjectFromResource("search/pagedSearchResult.json", ProductProjectionSearch.resultTypeReference());
    }
}
