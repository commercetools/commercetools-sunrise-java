package com.commercetools.sunrise.search.facetedsearch;

public class RangeFacetedSearchSelectorFactoryTest {

    private static final String KEY = "key";
    private static final String LABEL = "Some Facet";

//    @Test
//    public void initializesFacet() throws Exception {
//        final RangeFacetedSearchConfig config = RangeFacetedSearchConfig.of(facetBuilder("attr"), 1);
//        final List<String> selectedValues = asList("0 to 1000", "1000 to 2000");
//        test(config, selectedValues, facetSelector -> {
//            final Facet<ProductProjection> facet = facetSelector.getFacet(searchResult());
//            assertThat(facet).as("class").isInstanceOf(RangeFacet.class);
//            final RangeFacet<ProductProjection> rangeFacet = (RangeFacet<ProductProjection>) facet;
//            assertThat(rangeFacet.getName()).as("type").isEqualTo(SunriseFacetUIType.LIST);
//            assertThat(rangeFacet.getKey()).as("key").isEqualTo(KEY);
//            assertThat(rangeFacet.getFieldLabel()).as("label").isEqualTo(LABEL);
//            assertThat(rangeFacet.isCountHidden()).as("count hidden").isTrue();
//            assertThat(rangeFacet.getSelectedRanges()).as("selected values").isEqualTo(asList(FilterRange.of("0", "1000"), FilterRange.of("1000", "2000")));
//        });
//    }
//
//    @Test
//    public void getsFacetedSearchExprWithSelectedValue() throws Exception {
//        final RangeFacetedSearchConfig config = RangeFacetedSearchConfig.of(facetBuilder("attr"), 1);
//        final List<String> selectedValues = asList("0 to 1000", "1000 to 2000");
//        test(config, selectedValues, facetSelector -> {
//            assertThat(facetSelector.getFacetedSearchExpression().facetExpression().expression()).isEqualTo("attr:range(\"0\" to *)");
//            assertThat(facetSelector.getFacetedSearchExpression().filterExpressions())
//                    .extracting(FilterExpression::expression)
//                    .containsOnly("attr:range(\"0\" to \"1000\"),(\"1000\" to \"2000\")");
//        });
//    }
//
//    @Test
//    public void getsFacetedSearchExprWithLocale() throws Exception {
//        final RangeFacetedSearchConfig config = RangeFacetedSearchConfig.of(facetBuilder("some.{{locale}}.foo.{{locale}}.bar"), 1);
//        final List<String> selectedValues = emptyList();
//        test(config, selectedValues,  facetSelector -> {
//            assertThat(facetSelector.getFacetedSearchExpression().facetExpression().expression()).isEqualTo("some.en.foo.en.bar:range(\"0\" to *)");
//            assertThat(facetSelector.getFacetedSearchExpression().filterExpressions()).isEmpty();
//        });
//    }
//
//    private RangeFacetBuilder<ProductProjection> facetBuilder(final String attrPath) {
//        return RangeFacetBuilder.of(KEY, RangeTermFacetedSearchSearchModel.<ProductProjection>of(attrPath))
//                .label(LABEL)
//                .type(SunriseFacetUIType.LIST)
//                .countHidden(true)
//                .initialRanges(asList(FacetRange.atLeast("0")));
//    }
//
//    private void test(final RangeFacetedSearchConfig config, final List<String> selectedValues, final Consumer<FacetedSearchSelector> test) {
//        final Map<String, List<String>> queryString = singletonMap(config.getFacetBuilder().getKey(), selectedValues);
//        final Http.Request httpRequest = fakeRequest().uri(QueryStringUtils.buildUri("", queryString)).build();
//        final RangeFacetedSearchSelectorFactory factory = new RangeFacetedSearchSelectorFactory(Locale.ENGLISH, httpRequest);
//        test.accept(factory.create(config));
//    }
//
//    private static PagedSearchResult<ProductProjection> searchResult() {
//        return readObjectFromResource("search/pagedSearchResult.json", ProductProjectionSearch.resultTypeReference());
//    }
}

