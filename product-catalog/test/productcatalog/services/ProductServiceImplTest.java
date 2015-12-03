package productcatalog.services;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import org.junit.Test;
import play.libs.F;

import java.util.List;

import static common.categories.JsonUtils.readJson;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductServiceImplTest {
    private static final PagedQueryResult<ProductProjection> PRODUCTS = readJson("productProjectionQueryResult.json", ProductProjectionQuery.resultTypeReference());
    private static final List<Category> CATEGORIES = readJson("categoryQueryResult.json", CategoryQuery.resultTypeReference()).getResults();
    private static final int DEFAULT_TIMEOUT = 1000;

    @Test
    public void getSuggestions() {
        final ProductService service = new ProductServiceImpl(getSphereClientReturningAllProducts());
        assertThat(service.getSuggestions(CATEGORIES, 4).get(DEFAULT_TIMEOUT)).hasSize(4).doesNotHaveDuplicates();
        assertThat(service.getSuggestions(CATEGORIES, 5).get(DEFAULT_TIMEOUT)).hasSize(5).doesNotHaveDuplicates();
    }

    private PlayJavaSphereClient getSphereClientReturningAllProducts() {
        return new PlayJavaSphereClient() {
            @Override
            public void close() {

            }

            @Override
            @SuppressWarnings("unchecked")
            public <T> F.Promise<T> execute(SphereRequest<T> sphereRequest) {
                return (F.Promise<T>)F.Promise.pure(PRODUCTS);
            }
        };
    }
}
