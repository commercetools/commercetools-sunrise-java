package productcatalog.productdetail;

import java.util.concurrent.CompletionStage;

public interface ProductFetch<P, V> {

    CompletionStage<ProductFetchResult> findProduct(final P productIdentifier, final V variantIdentifier);
}
