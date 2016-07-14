package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.models.ProductAttributeBean;
import com.commercetools.sunrise.common.models.ProductAttributeBeanFactory;
import io.sphere.sdk.carts.LineItem;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class LineItemBeanFactory extends MiniCartLineItemBeanFactory {
    @Inject
    private ProductDataConfig productDataConfig;
    @Inject
    private ProductAttributeBeanFactory productAttributeBeanFactory;

    public LineItemBean create(final LineItem lineItem) {
        final LineItemBean bean = fillBean(new LineItemBean(), lineItem);
        final List<ProductAttributeBean> attributes = lineItem.getVariant().getAttributes().stream()
                .filter(attr -> productDataConfig.getSelectableAttributes().contains(attr.getName()))
                .map(attr -> productAttributeBeanFactory.create(attr))
                .collect(toList());
        bean.setAttributes(attributes);
        return bean;
    }
}
