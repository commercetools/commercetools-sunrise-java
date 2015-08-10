package productcatalog.common;

import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.shippingmethods.queries.ShippingMethodQuery;

import java.util.List;

import static io.sphere.sdk.json.JsonUtils.readObjectFromResource;

public class ShippingMethodTestData {

    private final PagedQueryResult<ShippingMethod> queryResult =
            readObjectFromResource("shippingMethodQueryResult.json", ShippingMethodQuery.resultTypeReference());

    private ShippingMethodTestData() {

    }

    public static ShippingMethodTestData of() {
        return new ShippingMethodTestData();
    }

    public List<ShippingMethod> getShippingMethods() {
        return queryResult.getResults();
    }
}
