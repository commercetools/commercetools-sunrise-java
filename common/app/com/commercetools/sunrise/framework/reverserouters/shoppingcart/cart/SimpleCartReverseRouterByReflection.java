package com.commercetools.sunrise.framework.reverserouters.shoppingcart.cart;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class SimpleCartReverseRouterByReflection extends AbstractReflectionReverseRouter implements SimpleCartReverseRouter {

    private final ReverseCaller showCart;
    private final ReverseCaller addLineItemProcessCaller;
    private final ReverseCaller changeLineItemQuantityProcessCaller;
    private final ReverseCaller removeLineItemProcessCaller;
    private final ReverseCaller addDiscountCodeProcessCaller;
    private final ReverseCaller removeDiscountCodeProcessCaller;

    @Inject
    private SimpleCartReverseRouterByReflection(final ParsedRoutes parsedRoutes) {
        showCart = getReverseCallerForSunriseRoute(CART_DETAIL_PAGE, parsedRoutes);
        addLineItemProcessCaller = getReverseCallerForSunriseRoute(ADD_LINE_ITEM_PROCESS, parsedRoutes);
        changeLineItemQuantityProcessCaller = getReverseCallerForSunriseRoute(CHANGE_LINE_ITEM_QUANTITY_PROCESS, parsedRoutes);
        removeLineItemProcessCaller = getReverseCallerForSunriseRoute(REMOVE_LINE_ITEM_PROCESS, parsedRoutes);
        addDiscountCodeProcessCaller = getReverseCallerForSunriseRoute(ADD_DISCOUNT_CODE_PROCESS, parsedRoutes);
        removeDiscountCodeProcessCaller = getReverseCallerForSunriseRoute(REMOVE_DISCOUNT_CODE_PROCESS, parsedRoutes);
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

    @Override
    public Call addDiscountCodeProcessCall(final String languageTag) {
        return addDiscountCodeProcessCaller.call(languageTag);
    }

    @Override
    public Call removeDiscountCodeProcessCall(final String languageTag) {
        return removeDiscountCodeProcessCaller.call(languageTag);
    }
}
