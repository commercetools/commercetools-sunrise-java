package productcatalog.productsuggestions;

import common.contexts.UserContext;
import common.controllers.SunrisePageData;
import common.hooks.SunrisePageDataHook;
import common.suggestion.ProductRecommendation;
import framework.ControllerComponent;
import io.sphere.sdk.products.ProductProjection;
import play.Configuration;
import productcatalog.common.ProductListBean;
import productcatalog.common.ProductListBeanFactory;
import productcatalog.common.SuggestionsData;
import productcatalog.hooks.SingleProductProjectionHook;
import productcatalog.productdetail.ProductDetailPageContent;

import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.CompletionStage;

/* (non-Javadoc)
 * this component is used as reference component to document how components work
 */

/**
 * Loads some other products that are related to the loaded product in the controller.
 */
public class ProductSuggestionsControllerComponent implements ControllerComponent, SingleProductProjectionHook, SunrisePageDataHook {

    //this part contains fields which are initialized by Google Guice and mostly are in the scope of the HTTP request
    @Inject
    protected UserContext userContext;
    //an example for a singleton scope dependency
    @Inject
    protected ProductRecommendation productRecommendation;
    //an example for a factory in request scope
    @Inject
    protected ProductListBeanFactory productListBeanFactory;

    //this value is indirectly filled when "setConfig" is called via Guice
    protected int numSuggestions;

    //here are fields which are not injected via dependency injection
    //it is a stash of values collected within the hooks
    protected Set<ProductProjection> suggestions;

    /* (non-Javadoc)
     * setter injection for the configuration
     * you can override it in a subclass like
<pre>{@code
    @Override
    @Inject
    public void setConfig(final Configuration configuration) {
        super.setConfig(configuration.getConfig("config-prefix-key"));
    }
}</pre>
     */
    @Inject
    public void setConfig(final Configuration configuration) {
        this.numSuggestions = configuration.getInt("productSuggestions.count");
    }

    /* (non-Javadoc)
        Implements the hook SingleProductProjectionHook and stores the result inside the field "suggestions".
     */
    @Override
    public CompletionStage<?> onSingleProductProjectionLoaded(final ProductProjection product) {
        return productRecommendation.relatedToProduct(product, numSuggestions, userContext)
                .thenAccept(m -> suggestions = m);//this method needs to return a CompletionStage which is completed when everything is done.
    }

    /* (non-Javadoc)
      Implements the hook SunrisePageDataHook which is executed after all asynchronous tasks related to this HTTP request are completed.
    */
    @Override
    public void acceptSunrisePageData(final SunrisePageData sunrisePageData) {
        //it is a very good practice to check if the field is not null before using it
        //in this example it is required that the content is of type ProductDetailPageContent
        if (suggestions != null && sunrisePageData.getContent() instanceof ProductDetailPageContent) {
            final ProductDetailPageContent content = (ProductDetailPageContent) sunrisePageData.getContent();
            content.setSuggestions(createSuggestions(suggestions));
        }
    }

    protected SuggestionsData createSuggestions(final Set<ProductProjection> suggestions) {
        final ProductListBean productListData = productListBeanFactory.create(suggestions);
        return new SuggestionsData(productListData);
    }
}
