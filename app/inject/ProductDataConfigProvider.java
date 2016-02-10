package inject;

import common.models.ProductDataConfig;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.producttypes.MetaProductType;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.queries.ProductTypeQuery;
import io.sphere.sdk.queries.QueryAll;
import play.Configuration;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

import static java.util.Collections.emptyList;

class ProductDataConfigProvider extends Base implements Provider<ProductDataConfig> {
    private static final String CONFIG_DISPLAYED_ATTRS = "productData.displayedAttributes";
    private static final String CONFIG_SELECTABLE_ATTRS = "productData.selectableAttributes";
    private final Configuration configuration;
    private final SphereClient client;

    @Inject
    public ProductDataConfigProvider(final Configuration configuration, final SphereClient client) {
        this.configuration = configuration;
        this.client = client;
    }

    @Override
    public ProductDataConfig get() {
        final List<String> displayedAttributes = configuration.getStringList(CONFIG_DISPLAYED_ATTRS, emptyList());
        final List<String> selectableAttributes = configuration.getStringList(CONFIG_SELECTABLE_ATTRS, emptyList());
        Logger.debug("Provide ProductDataConfig: displayed attributes {}, selectable attributes {}", displayedAttributes, selectableAttributes);
        return ProductDataConfig.of(getMetaProductType(), displayedAttributes, selectableAttributes);
    }

    private MetaProductType getMetaProductType() {
        final List<ProductType> productTypes =  QueryAll.of(ProductTypeQuery.of()).run(client).toCompletableFuture().join();
        return MetaProductType.of(productTypes);
    }
}
