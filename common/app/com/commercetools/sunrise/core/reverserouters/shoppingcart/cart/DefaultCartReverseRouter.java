package com.commercetools.sunrise.core.reverserouters.shoppingcart.cart;

import com.commercetools.sunrise.core.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.core.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.core.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DefaultCartReverseRouter extends AbstractReflectionReverseRouter implements CartReverseRouter {

    private final ReverseCaller showCart;
    private final ReverseCaller addLineItemProcessCaller;
    private final ReverseCaller changeLineItemQuantityProcessCaller;
    private final ReverseCaller removeLineItemProcessCaller;
    private final ReverseCaller addDiscountCodeProcessCaller;
    private final ReverseCaller removeDiscountCodeProcessCaller;

    @Inject
    protected DefaultCartReverseRouter(final ParsedRoutes parsedRoutes) {
        showCart = getReverseCallerForSunriseRoute(CART_DETAIL_PAGE, parsedRoutes);
        addLineItemProcessCaller = getReverseCallerForSunriseRoute(ADD_LINE_ITEM_PROCESS, parsedRoutes);
        changeLineItemQuantityProcessCaller = getReverseCallerForSunriseRoute(CHANGE_LINE_ITEM_QUANTITY_PROCESS, parsedRoutes);
        removeLineItemProcessCaller = getReverseCallerForSunriseRoute(REMOVE_LINE_ITEM_PROCESS, parsedRoutes);
        addDiscountCodeProcessCaller = getReverseCallerForSunriseRoute(ADD_DISCOUNT_CODE_PROCESS, parsedRoutes);
        removeDiscountCodeProcessCaller = getReverseCallerForSunriseRoute(REMOVE_DISCOUNT_CODE_PROCESS, parsedRoutes);
    }

    @Override
    public Call cartDetailPageCall() {
        return showCart.call();
    }

    @Override
    public Call addLineItemProcessCall() {
        return addLineItemProcessCaller.call();
    }

    @Override
    public Call changeLineItemQuantityProcessCall() {
        return changeLineItemQuantityProcessCaller.call();
    }

    @Override
    public Call removeLineItemProcessCall() {
        return removeLineItemProcessCaller.call();
    }

    @Override
    public Call addDiscountCodeProcessCall() {
        return addDiscountCodeProcessCaller.call();
    }

    @Override
    public Call removeDiscountCodeProcessCall() {
        return removeDiscountCodeProcessCaller.call();
    }
}
