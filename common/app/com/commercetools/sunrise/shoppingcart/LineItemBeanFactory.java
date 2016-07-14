package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.models.ProductAttributeBean;
import com.commercetools.sunrise.common.tobedeleted.ProductAttributeBeanFactoryInjectless;
import io.sphere.sdk.carts.LineItem;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class LineItemBeanFactory extends MiniCartLineItemBeanFactory {
    @Inject
    private ProductDataConfig productDataConfig;
    @Inject
    private UserContext userContext;

    public LineItemBean create(final LineItem lineItem) {
        final LineItemBean bean = fillbean(new LineItemBean(), lineItem);
        final List<ProductAttributeBean> attributes = lineItem.getVariant().getAttributes().stream()
                .filter(attr -> productDataConfig.getSelectableAttributes().contains(attr.getName()))
                .map(attr -> ProductAttributeBeanFactoryInjectless.create(attr, userContext, productDataConfig))
                .collect(toList());
        bean.setAttributes(attributes);
        return bean;
    }
}
