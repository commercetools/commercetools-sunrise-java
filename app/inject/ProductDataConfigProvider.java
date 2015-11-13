package inject;

import common.models.ProductDataConfig;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.producttypes.MetaProductType;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.queries.ProductTypeQuery;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public class ProductDataConfigProvider extends Base implements Provider<ProductDataConfig> {
    private final Configuration configuration;
    private final SphereClient client;

    @Inject
    public ProductDataConfigProvider(final Configuration configuration, final SphereClient client) {
        this.configuration = configuration;
        this.client = client;
    }

    @Override
    public ProductDataConfig get() {
        final List<ProductType> productTypes = client.execute(ProductTypeQuery.of().withLimit(500)).toCompletableFuture().join().getResults();
        final MetaProductType metaProductType = MetaProductType.of(productTypes);
        final List<String> attributesWhitelist = configuration.getStringList("productData.enabledAttributes");
        return new ProductDataConfig(metaProductType, attributesWhitelist);
    }
}
