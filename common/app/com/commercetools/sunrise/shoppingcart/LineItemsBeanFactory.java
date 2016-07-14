package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.Base;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class LineItemsBeanFactory extends Base {
    @Inject
    protected UserContext userContext;
    @Inject
    protected ProductDataConfig productDataConfig;
    @Inject
    private LineItemBeanFactory lineItemBeanFactory;

    public LineItemsBean create(final List<LineItem> lineItems) {
        return fillBean(new LineItemsBean(), lineItems);
    }

    protected <T extends LineItemsBean> T fillBean(final T bean, final List<LineItem> lineItems) {
        final List<LineItemBean> lineItemBeanList = lineItems.stream()
                .map(lineItem -> lineItemBeanFactory.create(lineItem))
                .collect(Collectors.toList());
        bean.setList(lineItemBeanList);
        return bean;
    }
}
