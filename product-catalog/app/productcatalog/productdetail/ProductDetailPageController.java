package productcatalog.productdetail;

import common.inject.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public final class ProductDetailPageController extends SunriseProductDetailPageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SunriseProductDetailPageController.class);

//    protected CompletionStage<SuggestionsData> createSuggestions(final ProductProjection product, final UserContext userContext) {
//        return productRecommendation().relatedToProduct(product, numSuggestions)
//                .thenAcceptAsync(suggestions -> {
//                    final ProductListData productListData = new ProductListData(suggestions, productDataConfig(), userContext, reverseRouter(), categoryTreeInNew());
//                    return new SuggestionsData(productListData);
//                });
//    }

    //        content.setSuggestions(createSuggestions(userContext, suggestions));


}