package inject;

import com.google.inject.Provider;
import io.sphere.sdk.client.PlayJavaSphereClient;
import play.Logger;
import productcatalog.services.ProductService;
import productcatalog.services.ProductServiceImpl;

import javax.inject.Inject;

class ProductProjectionServiceProvider implements Provider<ProductService> {
    private final PlayJavaSphereClient sphere;

    @Inject
    public ProductProjectionServiceProvider(final PlayJavaSphereClient sphere) {
        this.sphere = sphere;
    }

    @Override
    public ProductService get() {
        Logger.debug("Provide ProductProjectionService");
        return new ProductServiceImpl(sphere);
    }
}
