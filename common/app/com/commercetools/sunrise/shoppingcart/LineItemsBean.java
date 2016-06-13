package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.Base;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;

import java.util.List;
import java.util.stream.Collectors;

public class LineItemsBean extends Base {

    private List<LineItemBean> list;

    public LineItemsBean() {
    }

    public LineItemsBean(final List<LineItem> lineItems, final ProductDataConfig productDataConfig,
                         final UserContext userContext, final ProductReverseRouter reverseRouter) {
        this.list = lineItems.stream()
                .map(lineItem -> new LineItemBean(lineItem, productDataConfig, userContext, reverseRouter))
                .collect(Collectors.toList());
    }

    public List<LineItemBean> getList() {
        return list;
    }

    public void setList(final List<LineItemBean> list) {
        this.list = list;
    }
}
