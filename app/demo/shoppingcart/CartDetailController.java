package demo.shoppingcart;

import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.SunriseCartDetailController;

import javax.inject.Inject;

public class CartDetailController extends SunriseCartDetailController {

    @Inject
    public CartDetailController(final CartFinder cartFinder, final CartDetailPageContentFactory cartDetailPageContentFactory) {
        super(cartFinder, cartDetailPageContentFactory);
    }
}
