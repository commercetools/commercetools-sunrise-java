package productcatalog.common;

import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.List;

import static io.sphere.sdk.json.JsonUtils.readObjectFromResource;

public class ProductTestData {

    private final PagedQueryResult<ProductProjection> queryResult =
            readObjectFromResource("productProjectionQueryResult.json", ProductProjectionQuery.resultTypeReference());

    private ProductTestData() {

    }

    public static ProductTestData of() {
        return new ProductTestData();
    }

    public List<ProductProjection> getProducts() {
        return queryResult.getResults();
    }
}
