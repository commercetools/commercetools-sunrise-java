package productcatalog.productdetail;

import common.inject.RequestScoped;
import productcatalog.productsuggestions.ProductSuggestionsControllerComponent;

import javax.inject.Inject;

@RequestScoped
public final class ProductDetailPageController extends SunriseProductDetailPageController {

    @Inject
    public void setSuggestionsComponent(final ProductSuggestionsControllerComponent component) {
        registerControllerComponent(component);
    }
}