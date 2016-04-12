package productcatalog.productoverview.search;

import com.neovisionaries.i18n.CountryCode;
import common.contexts.UserContext;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.facets.AlphabeticallySortedFacetOptionMapper;
import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.facets.SelectFacet;
import io.sphere.sdk.facets.SelectFacetBuilder;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.FilterExpression;
import io.sphere.sdk.search.PagedSearchResult;
import io.sphere.sdk.search.model.TermFacetedSearchSearchModel;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;

import javax.money.Monetary;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static io.sphere.sdk.json.SphereJsonUtils.readObjectFromResource;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static java.util.Locale.ENGLISH;
import static org.assertj.core.api.Assertions.assertThat;

public class SelectFacetSelectorFactoryTest {

    private static final String KEY = "key";
    private static final String LABEL = "Some Facet";

    @Test
    public void initializesFacet() throws Exception {
        final SelectFacetConfig config = SelectFacetConfig.of(facetBuilder("foo.bar").mapper(AlphabeticallySortedFacetOptionMapper.of()), 1);
        final List<String> selectedValues = asList("foo", "bar");
        test(config, selectedValues, facetSelector -> {
            final Facet<ProductProjection> facet = facetSelector.getFacet(searchResult());
            assertThat(facet).as("class").isInstanceOf(SelectFacet.class);
            final SelectFacet<ProductProjection> selectFacet = (SelectFacet<ProductProjection>) facet;
            assertThat(selectFacet.getType()).as("type").isEqualTo(SunriseFacetType.LIST);
            assertThat(selectFacet.getKey()).as("key").isEqualTo(KEY);
            assertThat(selectFacet.getLabel()).as("label").isEqualTo(LABEL);
            assertThat(selectFacet.isCountHidden()).as("count hidden").isTrue();
            assertThat(selectFacet.isMatchingAll()).as("matching all").isTrue();
            assertThat(selectFacet.isMultiSelect()).as("multi select").isFalse();
            assertThat(selectFacet.getLimit()).as("limit").isEqualTo(3L);
            assertThat(selectFacet.getThreshold()).as("threshold").isEqualTo(2L);
            assertThat(selectFacet.getMapper()).as("mapper").isInstanceOf(AlphabeticallySortedFacetOptionMapper.class);
            assertThat(selectFacet.getSelectedValues()).as("selected values").containsExactlyElementsOf(selectedValues);
        });
    }

    @Test
    public void getsFacetedSearchExprWithSelectedValueMatchingAll() throws Exception {
        final SelectFacetConfig config = SelectFacetConfig.of(facetBuilder("attr"), 1);
        final List<String> selectedValues = asList("foo", "bar");
        test(config, selectedValues, facetSelector -> {
            Assertions.assertThat(facetSelector.getFacetedSearchExpression().facetExpression().expression()).isEqualTo("attr");
            Assertions.assertThat(facetSelector.getFacetedSearchExpression().filterExpressions()).extracting(FilterExpression::expression).containsOnly("attr:\"foo\"", "attr:\"bar\"");
        });
    }

    @Test
    public void getsFacetedSearchExprWithSelectedValueMatchingAny() throws Exception {
        final SelectFacetConfig config = SelectFacetConfig.of(facetBuilder("attr").matchingAll(false), 1);
        final List<String> selectedValues = asList("foo", "bar");
        test(config, selectedValues, facetSelector -> {
            Assertions.assertThat(facetSelector.getFacetedSearchExpression().facetExpression().expression()).isEqualTo("attr");
            Assertions.assertThat(facetSelector.getFacetedSearchExpression().filterExpressions()).extracting(FilterExpression::expression).containsOnly("attr:\"foo\",\"bar\"");
        });
    }

    @Ignore
    @Test
    public void getsFacetedSearchExprWithLocale() throws Exception {
        final SelectFacetConfig config = SelectFacetConfig.of(facetBuilder("some.{{locale}}.foo.{{locale}}.bar"), 1);
        final List<String> selectedValues = emptyList();
        test(config, selectedValues,  facetSelector -> {
            Assertions.assertThat(facetSelector.getFacetedSearchExpression().facetExpression().expression()).isEqualTo("some.en.foo.en.bar");
            Assertions.assertThat(facetSelector.getFacetedSearchExpression().filterExpressions()).isEmpty();
        });
    }

    private SelectFacetBuilder<ProductProjection> facetBuilder(final String attrPath) {
        return SelectFacetBuilder.of(KEY, TermFacetedSearchSearchModel.<ProductProjection>of(attrPath))
                .label(LABEL)
                .type(SunriseFacetType.LIST)
                .countHidden(true)
                .matchingAll(true)
                .multiSelect(false)
                .limit(3L)
                .threshold(2L);
    }

    private void test(final SelectFacetConfig config, final List<String> selectedValues, final Consumer<FacetSelector> test) {
        final Map<String, List<String>> queryString = singletonMap(config.getFacetBuilder().getKey(), selectedValues);
        final UserContext userContext = UserContext.of(singletonList(ENGLISH), CountryCode.DE, Monetary.getCurrency("EUR"));
        final SelectFacetSelectorFactory factory = SelectFacetSelectorFactory.of(config, queryString, emptyList(), userContext, CategoryTree.of(emptyList()));
        test.accept(factory.create());
    }

    private static PagedSearchResult<ProductProjection> searchResult() {
        return readObjectFromResource("search/pagedSearchResult.json", ProductProjectionSearch.resultTypeReference());
    }
}
