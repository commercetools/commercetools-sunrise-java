package com.commercetools.sunrise.framework.reverserouters.shoppingcart.cart;

import com.commercetools.sunrise.framework.reverserouters.ReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(SimpleCartReverseRouterByReflection.class)
public interface SimpleCartReverseRouter extends ReverseRouter {

    String CART_DETAIL_PAGE = "cartDetailPageCall";

    Call cartDetailPageCall(final String languageTag);

    String ADD_LINE_ITEM_PROCESS = "addLineItemProcessCall";

    Call addLineItemProcessCall(final String languageTag);

    String CHANGE_LINE_ITEM_QUANTITY_PROCESS = "changeLineItemQuantityProcessCall";

    Call changeLineItemQuantityProcessCall(final String languageTag);

    String REMOVE_LINE_ITEM_PROCESS = "removeLineItemProcessCall";

    Call removeLineItemProcessCall(final String languageTag);

    String ADD_DISCOUNT_CODE_PROCESS = "addDiscountCodeProcessCall";

    Call addDiscountCodeProcessCall(final String languageTag);

    String REMOVE_DISCOUNT_CODE_PROCESS = "removeDiscountCodeProcessCall";

    Call removeDiscountCodeProcessCall(final String languageTag);
}
