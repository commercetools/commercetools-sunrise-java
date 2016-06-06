package productcatalog.hooks;

import io.sphere.sdk.products.ProductProjection;

import java.util.concurrent.CompletionStage;

public interface SingleProductProjectionHook {
    CompletionStage<?> onSingleProductProjectionLoaded(final ProductProjection product);
}
