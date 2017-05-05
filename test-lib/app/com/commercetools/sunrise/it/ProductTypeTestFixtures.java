package com.commercetools.sunrise.it;

import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.ProductTypeDraft;
import io.sphere.sdk.producttypes.ProductTypeDraftBuilder;
import io.sphere.sdk.producttypes.commands.ProductTypeCreateCommand;
import io.sphere.sdk.producttypes.commands.ProductTypeDeleteCommand;

import java.util.function.Function;

import static com.commercetools.sunrise.it.TestFixtures.*;
import static java.util.Collections.emptyList;

public final class ProductTypeTestFixtures {

    private ProductTypeTestFixtures() {
    }

    public static void withProductType(final BlockingSphereClient client, final ProductTypeDraft productTypeDraft, final Function<ProductType, ProductType> test) {
        final ProductType productType = client.executeBlocking(ProductTypeCreateCommand.of(productTypeDraft));
        final ProductType productTypeAfterTest = test.apply(productType);
        deleteProductTypeWithRetry(client, productTypeAfterTest);
    }

    public static ProductTypeDraft productTypeDraft() {
        return ProductTypeDraftBuilder.of(randomKey(), randomKey(), "Description", emptyList()).build();
    }

    public static void deleteProductTypeWithRetry(final BlockingSphereClient client, final ProductType productTypeAfterTest) {
        deleteWithRetry(client, productTypeAfterTest, ProductTypeDeleteCommand::of, DEFAULT_DELETE_TTL);
    }
}
