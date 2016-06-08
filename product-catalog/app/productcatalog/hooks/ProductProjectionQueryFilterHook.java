package productcatalog.hooks;

import common.hooks.Hook;
import io.sphere.sdk.products.queries.ProductProjectionQuery;

public interface ProductProjectionQueryFilterHook extends Hook {
    ProductProjectionQuery filterProductProjectionQuery(ProductProjectionQuery query);
}
