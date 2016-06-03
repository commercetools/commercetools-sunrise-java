package productcatalog.productdetail;

import common.controllers.ControllerDependency;
import common.inject.HttpContextScoped;
import common.suggestion.ProductRecommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@HttpContextScoped
public final class ProductDetailPageController extends SunriseProductDetailPageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SunriseProductDetailPageController.class);

    @Inject
    private ProductRecommendation productRecommendation;

    @Inject
    public ProductDetailPageController(final ControllerDependency controllerDependency) {
        super(controllerDependency);
    }

//    protected CompletionStage<SuggestionsData> createSuggestions(final ProductProjection product, final UserContext userContext) {
//        return productRecommendation().relatedToProduct(product, numSuggestions)
//                .thenAcceptAsync(suggestions -> {
//                    final ProductListData productListData = new ProductListData(suggestions, productDataConfig(), userContext, reverseRouter(), categoryTreeInNew());
//                    return new SuggestionsData(productListData);
//                });
//    }

    //        content.setSuggestions(createSuggestions(userContext, suggestions));


}