package productcatalog.services;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTreeExtended;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import org.junit.Test;
import play.libs.F;

import java.util.List;

import static common.JsonUtils.readCtpObject;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductServiceTest {
    private static final PagedSearchResult<ProductProjection> PRODUCTS = readCtpObject("productProjectionQueryResult.json", ProductProjectionSearch.resultTypeReference());
    private static final List<Category> CATEGORIES = readCtpObject("categoryQueryResult.json", CategoryQuery.resultTypeReference()).getResults();
    private static final int DEFAULT_TIMEOUT = 1000;

    @Test
    public void getSuggestions() {
        final ProductService productService = new ProductServiceImpl(clientReturningProducts(PRODUCTS));
        final CategoryTreeExtended categoryTree = CategoryTreeExtended.of(CATEGORIES);
        final ProductProjection product = PRODUCTS.getResults().get(3);
        assertThat(productService.getSuggestions(product, categoryTree, 4).get(DEFAULT_TIMEOUT))
                .hasSize(4)
                .doesNotHaveDuplicates();
        assertThat(productService.getSuggestions(product, categoryTree, 5).get(DEFAULT_TIMEOUT))
                .hasSize(5)
                .doesNotHaveDuplicates();
    }

    private static PlayJavaSphereClient clientReturningProducts(final PagedSearchResult<ProductProjection> products) {
        return new PlayJavaSphereClient() {
            @Override
            public void close() {

            }

            @Override
            @SuppressWarnings("unchecked")
            public <T> F.Promise<T> execute(SphereRequest<T> sphereRequest) {
                return (F.Promise<T>)F.Promise.pure(products);
            }
        };
    }
}
