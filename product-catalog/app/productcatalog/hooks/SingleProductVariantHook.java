package productcatalog.hooks;

import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import java.util.concurrent.CompletionStage;

public interface SingleProductVariantHook {
    CompletionStage<?> onSingleProductVariantLoaded(final ProductProjection product, final ProductVariant variant);
}
