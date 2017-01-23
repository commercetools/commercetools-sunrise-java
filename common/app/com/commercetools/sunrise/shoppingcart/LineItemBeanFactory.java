package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.ProductVariantBeanFactory;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.LineItem;

import javax.inject.Inject;

@RequestScoped
public class LineItemBeanFactory extends ViewModelFactory {

    private final PriceFormatter priceFormatter;
    private final ProductVariantBeanFactory productVariantBeanFactory;

    @Inject
    public LineItemBeanFactory(final PriceFormatter priceFormatter, final ProductVariantBeanFactory productVariantBeanFactory) {
        this.priceFormatter = priceFormatter;
        this.productVariantBeanFactory = productVariantBeanFactory;
    }

    public LineItemBean create(final LineItem lineItem) {
        final LineItemBean bean = new LineItemBean();
        initialize(bean, lineItem);
        return bean;
    }

    protected final void initialize(final LineItemBean bean, final LineItem lineItem) {
        fillTotalPrice(bean, lineItem);
        fillLineItemId(bean, lineItem);
        fillQuantity(bean, lineItem);
        fillVariant(bean, lineItem);
    }

    protected void fillLineItemId(final LineItemBean bean, final LineItem lineItem) {
        bean.setLineItemId(lineItem.getId());
    }

    protected void fillQuantity(final LineItemBean bean, final LineItem lineItem) {
        bean.setQuantity(lineItem.getQuantity());
    }

    protected void fillVariant(final LineItemBean bean, final LineItem lineItem) {
        bean.setVariant(productVariantBeanFactory.create(lineItem));
    }

    protected void fillTotalPrice(final LineItemBean bean, final LineItem lineItem) {
        bean.setTotalPrice(priceFormatter.format(lineItem.getTotalPrice()));
    }
}
