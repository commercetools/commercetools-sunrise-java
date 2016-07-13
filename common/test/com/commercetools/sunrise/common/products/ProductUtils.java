package com.commercetools.sunrise.common.products;

import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.List;

import static io.sphere.sdk.json.SphereJsonUtils.readObjectFromResource;

public class ProductUtils {

    public static PagedQueryResult<ProductProjection> getQueryResult(final String resourceName) {
        return readObjectFromResource(resourceName, ProductProjectionQuery.resultTypeReference());
    }

    public static ProductProjection getProductById(final List<ProductProjection> products, final String id) {
        return products.stream().filter(projection -> projection.getId().equals(id)).findAny().get();
    }
}
