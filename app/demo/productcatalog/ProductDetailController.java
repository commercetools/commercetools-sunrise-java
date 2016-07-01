package demo.productcatalog;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.productcatalog.productdetail.SunriseProductDetailController;
import com.commercetools.sunrise.productcatalog.productsuggestions.ProductSuggestionsControllerComponent;

import javax.inject.Inject;

@RequestScoped
public class ProductDetailController extends SunriseProductDetailController {

    @Inject
    public void setSuggestionsComponent(final ProductSuggestionsControllerComponent component) {
        registerControllerComponent(component);
    }
}