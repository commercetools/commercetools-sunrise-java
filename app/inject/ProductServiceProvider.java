package inject;

import com.google.inject.Provider;
import io.sphere.sdk.client.SphereClient;
import play.Logger;
import productcatalog.services.ProductService;
import productcatalog.services.ProductServiceImpl;

import javax.inject.Inject;

class ProductServiceProvider implements Provider<ProductService> {
    private final SphereClient sphere;

    @Inject
    public ProductServiceProvider(final SphereClient sphere) {
        this.sphere = sphere;
    }

    @Override
    public ProductService get() {
        Logger.debug("Provide ProductService");
        return new ProductServiceImpl(sphere);
    }
}
