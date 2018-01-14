package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

public class ProductBreadcrumbViewModelFactoryTest {

//    private static final CategoryTree CATEGORY_TREE = CategoryTree.of(readCtpObject("breadcrumb/breadcrumbCategories.json", CategoryQuery.resultTypeReference()).getResults());
//    private static final ProductProjection PRODUCT = readCtpObject("breadcrumb/breadcrumbProduct.json", ProductProjection.typeReference());
//
//    @Test
//    public void createProductBreadcrumb() throws Exception {
//        testProductBreadcrumb(PRODUCT,
//                texts -> assertThat(texts).containsExactly("1st Level", "2nd Level", "Some product"),
//                urls -> assertThat(urls).containsExactly("1st-level", "2nd-level", "some-product-some-sku"));
//    }
//
//    private void testProductBreadcrumb(final ProductProjection product, final Consumer<List<String>> texts, final Consumer<List<String>> urls) {
//        final ProductWithVariant productWithVariant = ProductWithVariant.of(product, product.getMasterVariant());
//        final BreadcrumbViewModel breadcrumb = createBreadcrumbFactory().create(productWithVariant);
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
//    private static ProductBreadcrumbViewModelFactory createBreadcrumbFactory() {
//        final ProductReverseRouter productReverseRouter = mock(ProductReverseRouter.class);
//        when(productReverseRouter.productOverviewPageCall(any(Category.class)))
//                .then(invocation -> ((Category) invocation.getArgument(0)).getSlug()
//                        .find(Locale.ENGLISH)
//                        .map(TestableCall::new));
//        when(productReverseRouter.productDetailPageCall(any(ProductProjection.class), any(ProductVariant.class)))
//                .then(invocation -> ((ProductProjection) invocation.getArgument(0)).getSlug()
//                    .find(Locale.ENGLISH)
//                    .map(slug -> slug + ((ProductVariant) invocation.getArgument(1)).getSku())
//                    .map(TestableCall::new));
//        return new ProductBreadcrumbViewModelFactory(CATEGORY_TREE, productReverseRouter);
//    }
}
