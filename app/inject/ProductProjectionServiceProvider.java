package inject;

import com.google.inject.Provider;
import io.sphere.sdk.client.PlayJavaSphereClient;
import play.Logger;
import productcatalog.services.ProductProjectionService;
import productcatalog.services.ProductProjectionServiceImpl;

import javax.inject.Inject;

public class ProductProjectionServiceProvider implements Provider<ProductProjectionService> {
    private final PlayJavaSphereClient sphere;

    @Inject
    public ProductProjectionServiceProvider(final PlayJavaSphereClient sphere) {
        this.sphere = sphere;
    }

    @Override
    public ProductProjectionService get() {
        Logger.debug("Provide ProductProjectionService");
        return new ProductProjectionServiceImpl(sphere);
    }
}
