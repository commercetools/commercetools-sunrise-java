package com.commercetools.sunrise.framework.reverserouters.shoppingcart;

import com.commercetools.sunrise.framework.reverserouters.ReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionCartReverseRouter.class)
interface CartSimpleReverseRouter extends ReverseRouter {

    String CART_DETAIL_PAGE = "cartDetailPage";

    Call cartDetailPageCall(final String languageTag);

    String ADD_LINE_ITEM_PROCESS = "addLineItemProcess";

    Call addLineItemProcessCall(final String languageTag);

    String CHANGE_LINE_ITEM_QUANTITY_PROCESS = "changeLineItemQuantityProcess";

    Call changeLineItemQuantityProcessCall(final String languageTag);

    String REMOVE_LINE_ITEM_PROCESS = "removeLineItemProcess";

    Call removeLineItemProcessCall(final String languageTag);
}
