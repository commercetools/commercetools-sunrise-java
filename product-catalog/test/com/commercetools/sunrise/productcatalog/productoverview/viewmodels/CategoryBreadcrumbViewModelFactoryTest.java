package com.commercetools.sunrise.productcatalog.productoverview.viewmodels;

public class CategoryBreadcrumbViewModelFactoryTest {
//
//    private static final CategoryTree CATEGORY_TREE = CategoryTree.of(readCtpObject("breadcrumb/breadcrumbCategories.json", CategoryQuery.resultTypeReference()).getResults());
//
//    @Test
//    public void createCategoryBreadcrumbOfOneLevel() {
//        testCategoryBreadcrumb("1stLevel",
//                texts -> assertThat(texts).containsExactly("1st Level"),
//                urls -> assertThat(urls).containsExactly("1st-level"));
//    }
//
//    @Test
//    public void createCategoryBreadcrumbOfManyLevels() {
//        testCategoryBreadcrumb("3rdLevel",
//                texts -> assertThat(texts).containsExactly("1st Level", "2nd Level", "3rd Level"),
//                urls -> assertThat(urls).containsExactly("1st-level", "2nd-level", "3rd-level"));
//    }
//
//    private void testCategoryBreadcrumb(final String extId, final Consumer<List<String>> texts, final Consumer<List<String>> urls) {
//        final Category category = CATEGORY_TREE.findByExternalId(extId).get();
//        final ProductsWithCategory data = ProductsWithCategory.of(null, category);
//        final BreadcrumbViewModel breadcrumb = createBreadcrumbFactory().create(data);
//        testBreadcrumb(breadcrumb, texts, urls);
//    }
//
//    private void testBreadcrumb(final BreadcrumbViewModel breadcrumb, final Consumer<List<String>> texts, final Consumer<List<String>> urls) {
//        texts.accept(breadcrumb.getLinks().stream()
//                .map(BreadcrumbLinkViewModel::getText)
//                .map(link -> link.get(Locale.ENGLISH))
//                .collect(toList()));
//        urls.accept(breadcrumb.getLinks().stream().map(BreadcrumbLinkViewModel::getUrl).collect(toList()));
//    }
//
//    private static CategoryBreadcrumbViewModelFactory createBreadcrumbFactory() {
//        final ProductReverseRouter productReverseRouter = mock(ProductReverseRouter.class);
//        when(productReverseRouter.productOverviewPageCall(any(Category.class)))
//                .then(invocation -> ((Category) invocation.getArgument(0)).getSlug()
//                        .find(Locale.ENGLISH)
//                        .map(TestableCall::new));
//        return new CategoryBreadcrumbViewModelFactory(CATEGORY_TREE, productReverseRouter);
//    }
}
