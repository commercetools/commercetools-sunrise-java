package com.commercetools.sunrise.framework.reverserouters.shoppingcart;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRouteList;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionCartReverseRouter extends AbstractReflectionReverseRouter implements CartSimpleReverseRouter {

    private final ReverseCaller showCart;
    private final ReverseCaller addLineItemProcessCaller;
    private final ReverseCaller changeLineItemQuantityProcessCaller;
    private final ReverseCaller removeLineItemProcessCaller;

    @Inject
    private ReflectionCartReverseRouter(final ParsedRouteList parsedRouteList) {
        showCart = getCallerForRoute(parsedRouteList, CART_DETAIL_PAGE);
        addLineItemProcessCaller = getCallerForRoute(parsedRouteList, ADD_LINE_ITEM_PROCESS);
        changeLineItemQuantityProcessCaller = getCallerForRoute(parsedRouteList, CHANGE_LINE_ITEM_QUANTITY_PROCESS);
        removeLineItemProcessCaller = getCallerForRoute(parsedRouteList, REMOVE_LINE_ITEM_PROCESS);
    }
    @Override
    public Call cartDetailPageCall(final String languageTag) {
        return showCart.call(languageTag);
    }

    @Override
    public Call addLineItemProcessCall(final String languageTag) {
        return addLineItemProcessCaller.call(languageTag);
    }

    @Override
    public Call changeLineItemQuantityProcessCall(final String languageTag) {
        return changeLineItemQuantityProcessCaller.call(languageTag);
    }

    @Override
    public Call removeLineItemProcessCall(final String languageTag) {
        return removeLineItemProcessCaller.call(languageTag);
    }
}
