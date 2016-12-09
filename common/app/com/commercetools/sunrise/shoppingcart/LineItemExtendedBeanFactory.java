package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.models.ProductAttributeBean;
import com.commercetools.sunrise.common.models.ProductAttributeBeanFactory;
import com.commercetools.sunrise.common.models.ProductVariantBeanFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.LineItem;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class LineItemExtendedBeanFactory extends LineItemBeanFactory {

    private final ProductDataConfig productDataConfig;
    private final ProductAttributeBeanFactory productAttributeBeanFactory;

    @Inject
    public LineItemExtendedBeanFactory(final PriceFormatter priceFormatter, final ProductDataConfig productDataConfig,
                                       final ProductVariantBeanFactory productVariantBeanFactory,
                                       final ProductAttributeBeanFactory productAttributeBeanFactory) {
        super(priceFormatter, productVariantBeanFactory);
        this.productDataConfig = productDataConfig;
        this.productAttributeBeanFactory = productAttributeBeanFactory;
    }

    @Override
    public LineItemExtendedBean create(final LineItem lineItem) {
        final LineItemExtendedBean bean = new LineItemExtendedBean();
        initialize(bean, lineItem);
        return bean;
    }

    protected final void initialize(final LineItemExtendedBean bean, LineItem lineItem) {
        super.initialize(bean, lineItem);
        fillAttributes(bean, lineItem);
    }

    protected void fillAttributes(final LineItemExtendedBean bean, final LineItem lineItem) {
        final List<ProductAttributeBean> attributes = lineItem.getVariant().getAttributes().stream()
                .filter(attr -> productDataConfig.getSelectableAttributes().contains(attr.getName()))
                .map(productAttributeBeanFactory::create)
                .collect(toList());
        bean.setAttributes(attributes);
    }
}
