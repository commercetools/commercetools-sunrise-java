package com.commercetools.sunrise.core.reverserouters.shoppingcart.cart;

import com.commercetools.sunrise.core.reverserouters.ReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(DefaultCartReverseRouter.class)
public interface CartReverseRouter extends ReverseRouter {

    String CART_DETAIL_PAGE = "cartDetailPageCall";

    Call cartDetailPageCall();

    String ADD_LINE_ITEM_PROCESS = "addLineItemProcessCall";

    Call addLineItemProcessCall();

    String CHANGE_LINE_ITEM_QUANTITY_PROCESS = "changeLineItemQuantityProcessCall";

    Call changeLineItemQuantityProcessCall();

    String REMOVE_LINE_ITEM_PROCESS = "removeLineItemProcessCall";

    Call removeLineItemProcessCall();

    String ADD_DISCOUNT_CODE_PROCESS = "addDiscountCodeProcessCall";

    Call addDiscountCodeProcessCall();

    String REMOVE_DISCOUNT_CODE_PROCESS = "removeDiscountCodeProcessCall";

    Call removeDiscountCodeProcessCall();
}
