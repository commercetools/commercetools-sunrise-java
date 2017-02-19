package com.commercetools.sunrise.common.models.carts;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.LineItem;

public abstract class AbstractLineItemBeanFactory<T extends LineItemBean> extends ViewModelFactory<T, LineItem> {

    private final PriceFormatter priceFormatter;
    private final LineItemProductVariantBeanFactory lineItemProductVariantBeanFactory;

    protected AbstractLineItemBeanFactory(final PriceFormatter priceFormatter, final LineItemProductVariantBeanFactory lineItemProductVariantBeanFactory) {
        this.priceFormatter = priceFormatter;
        this.lineItemProductVariantBeanFactory = lineItemProductVariantBeanFactory;
    }

    @Override
    protected void initialize(final T model, final LineItem data) {
        fillLineItemId(model, data);
        fillQuantity(model, data);
        fillVariant(model, data);
        fillTotalPrice(model, data);
    }

    protected void fillLineItemId(final T model, final LineItem lineItem) {
        model.setLineItemId(lineItem.getId());
    }

    protected void fillQuantity(final T model, final LineItem lineItem) {
        model.setQuantity(lineItem.getQuantity());
    }

    protected void fillVariant(final T model, final LineItem lineItem) {
        model.setVariant(lineItemProductVariantBeanFactory.create(lineItem));
    }

    protected void fillTotalPrice(final T model, final LineItem lineItem) {
        model.setTotalPrice(priceFormatter.format(lineItem.getTotalPrice()));
    }
}
