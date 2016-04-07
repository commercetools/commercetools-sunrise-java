package productcatalog.productoverview;

import com.neovisionaries.i18n.CountryCode;
import common.contexts.UserContext;
import common.controllers.TestableReverseRouter;
import common.models.LinkData;
import common.controllers.ReverseRouter;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import org.junit.Test;
import productcatalog.common.BreadcrumbBean;

import java.util.List;
import java.util.function.Consumer;

import static common.JsonUtils.readCtpObject;
import static java.util.Collections.singletonList;
import static java.util.Locale.ENGLISH;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class BreadcrumbBeanTest {
    private static final CategoryTree CATEGORY_TREE = CategoryTree.of(readCtpObject("breadcrumb/breadcrumbCategories.json", CategoryQuery.resultTypeReference()).getResults());
    private static final ProductProjection PRODUCT = readCtpObject("breadcrumb/breadcrumbProduct.json", ProductProjection.typeReference());
    private static final UserContext USER_CONTEXT = UserContext.of(singletonList(ENGLISH), CountryCode.UK, null);
    private static final ReverseRouter REVERSE_ROUTER = reverseRouter();

    @Test
    public void createCategoryBreadcrumbOfOneLevel() {
        testCategoryBreadcrumb("1stLevel", CATEGORY_TREE,
                texts -> assertThat(texts).containsExactly("1st Level"),
                urls -> assertThat(urls).containsExactly("category-1st-level"));
    }

    @Test
    public void createCategoryBreadcrumbOfManyLevels() {
        testCategoryBreadcrumb("3rdLevel", CATEGORY_TREE,
                texts -> assertThat(texts).containsExactly("1st Level", "2nd Level", "3rd Level"),
                urls -> assertThat(urls).containsExactly("category-1st-level", "category-2nd-level", "category-3rd-level"));
    }

    @Test
    public void createProductBreadcrumb() throws Exception {
        testProductBreadcrumb("some-sku", PRODUCT,
                texts -> assertThat(texts).containsExactly("1st Level", "2nd Level", "Some product"),
                urls -> assertThat(urls).containsExactly("category-1st-level", "category-2nd-level", "product-some-product-some-sku"));
    }

    @Test
    public void createSearchBreadcrumb() throws Exception {
        final BreadcrumbBean breadcrumb = new BreadcrumbBean("foo");
        testBreadcrumb(breadcrumb,
                texts -> assertThat(texts).containsExactly("foo"),
                urls -> assertThat(urls).containsNull());
    }

    private static ReverseRouter reverseRouter() {
        final TestableReverseRouter reverseRouter = new TestableReverseRouter();
        reverseRouter.setShowCategoryUrl("category-");
        reverseRouter.setShowProductUrl("product-");
        reverseRouter.setProcessSearchProductsFormUrl("search-");
        return reverseRouter;
    }

    private void testCategoryBreadcrumb(final String extId, final CategoryTree categoryTree, final Consumer<List<String>> texts, final Consumer<List<String>> urls) {
        final Category category = categoryTree.findByExternalId(extId).get();
        final BreadcrumbBean breadcrumb = new BreadcrumbBean(category, CATEGORY_TREE, USER_CONTEXT, REVERSE_ROUTER);
        testBreadcrumb(breadcrumb, texts, urls);
    }

    private void testProductBreadcrumb(final String sku, final ProductProjection product, final Consumer<List<String>> texts, final Consumer<List<String>> urls) {
        final ProductVariant variant = product.findVariantBySku(sku).get();
        final BreadcrumbBean breadcrumb = new BreadcrumbBean(product, variant, CATEGORY_TREE, USER_CONTEXT, REVERSE_ROUTER);
        testBreadcrumb(breadcrumb, texts, urls);
    }

    private void testBreadcrumb(final BreadcrumbBean breadcrumb, final Consumer<List<String>> texts, final Consumer<List<String>> urls) {
        texts.accept(breadcrumb.getLinks().stream().map(LinkData::getText).collect(toList()));
        urls.accept(breadcrumb.getLinks().stream().map(LinkData::getUrl).collect(toList()));
    }
}
