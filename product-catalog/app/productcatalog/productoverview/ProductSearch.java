package productcatalog.productoverview;

import java.util.concurrent.CompletionStage;

public interface ProductSearch<C, S> {

    CompletionStage<ProductSearchResult> searchProducts(final C categoryIdentifier, final S searchCriteria);
}
