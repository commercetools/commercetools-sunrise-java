package com.commercetools.sunrise.common.reverserouter.shoppingcart;

import com.commercetools.sunrise.common.reverserouter.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.common.reverserouter.ReverseCaller;
import com.commercetools.sunrise.framework.ParsedRouteList;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionCartReverseRouter extends AbstractReflectionReverseRouter implements CartSimpleReverseRouter {

    private final ReverseCaller showCart;
    private final ReverseCaller processAddProductToCartForm;
    private final ReverseCaller processDeleteLineItemForm;
    private final ReverseCaller processChangeLineItemQuantityForm;

    @Inject
    private ReflectionCartReverseRouter(final ParsedRouteList parsedRouteList) {
        showCart = getCallerForRoute(parsedRouteList, "showCart");
        processAddProductToCartForm = getCallerForRoute(parsedRouteList, "processAddProductToCartForm");
        processDeleteLineItemForm = getCallerForRoute(parsedRouteList, "processDeleteLineItemForm");
        processChangeLineItemQuantityForm = getCallerForRoute(parsedRouteList, "processChangeLineItemQuantityForm");
    }
    @Override
    public Call showCart(final String languageTag) {
        return showCart.call(languageTag);
    }

    @Override
    public Call processAddProductToCartForm(final String languageTag) {
        return processAddProductToCartForm.call(languageTag);
    }

    @Override
    public Call processDeleteLineItemForm(final String languageTag) {
        return processDeleteLineItemForm.call(languageTag);
    }

    @Override
    public Call processChangeLineItemQuantityForm(final String languageTag) {
        return processChangeLineItemQuantityForm.call(languageTag);
    }
}
