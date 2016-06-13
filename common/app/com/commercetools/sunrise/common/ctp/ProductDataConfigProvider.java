package com.commercetools.sunrise.common.ctp;

import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.producttypes.MetaProductType;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.queries.ProductTypeQuery;
import play.Configuration;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.sphere.sdk.client.SphereClientUtils.blockingWait;
import static io.sphere.sdk.queries.QueryExecutionUtils.queryAll;
import static java.util.Collections.emptyList;

public final class ProductDataConfigProvider implements Provider<ProductDataConfig> {

    private static final String CONFIG_DISPLAYED_ATTRS = "productData.displayedAttributes";
    private static final String CONFIG_SOFT_SELECTABLE_ATTRS = "productData.softSelectableAttributes";
    private static final String CONFIG_HARD_SELECTABLE_ATTRS = "productData.hardSelectableAttributes";

    @Inject
    private Configuration configuration;
    @Inject
    private SphereClient client;

    @Override
    public ProductDataConfig get() {
        final List<String> displayedAttributes = configuration.getStringList(CONFIG_DISPLAYED_ATTRS, emptyList());
        final List<String> softSelectableAttributes = configuration.getStringList(CONFIG_SOFT_SELECTABLE_ATTRS, emptyList());
        final List<String> hardSelectableAttributes = configuration.getStringList(CONFIG_HARD_SELECTABLE_ATTRS, emptyList());
        Logger.debug("Provide ProductDataConfig: displayed attributes {}, soft selectable attributes {}, hard selectable attributes {}",
                displayedAttributes, softSelectableAttributes, hardSelectableAttributes);
        return ProductDataConfig.of(getMetaProductType(), displayedAttributes, softSelectableAttributes, hardSelectableAttributes);
    }

    private MetaProductType getMetaProductType() {
        final List<ProductType> productTypes = blockingWait(queryAll(client, ProductTypeQuery.of()), 30, TimeUnit.SECONDS);
        return MetaProductType.of(productTypes);
    }
}
