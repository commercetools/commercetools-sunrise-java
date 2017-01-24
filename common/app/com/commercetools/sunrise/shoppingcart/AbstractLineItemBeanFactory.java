package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.models.ProductVariantBeanFactory;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.Base;

public abstract class AbstractLineItemBeanFactory<T extends LineItemBean> extends ViewModelFactory<T, AbstractLineItemBeanFactory.Data> {

    private final PriceFormatter priceFormatter;
    private final ProductVariantBeanFactory productVariantBeanFactory;

    protected AbstractLineItemBeanFactory(final PriceFormatter priceFormatter, final ProductVariantBeanFactory productVariantBeanFactory) {
        this.priceFormatter = priceFormatter;
        this.productVariantBeanFactory = productVariantBeanFactory;
    }

    @Override
    protected void initialize(final T bean, final Data data) {
        fillLineItemId(bean, data);
        fillQuantity(bean, data);
        fillVariant(bean, data);
        fillTotalPrice(bean, data);
    }

    protected void fillLineItemId(final T bean, final Data data) {
        bean.setLineItemId(data.lineItem.getId());
    }

    protected void fillQuantity(final T bean, final Data data) {
        bean.setQuantity(data.lineItem.getQuantity());
    }

    protected void fillVariant(final T bean, final Data data) {
        bean.setVariant(productVariantBeanFactory.create(data.lineItem));
    }

    protected void fillTotalPrice(final T bean, final Data data) {
        bean.setTotalPrice(priceFormatter.format(data.lineItem.getTotalPrice()));
    }

    protected final static class Data extends Base {

        public final LineItem lineItem;

        public Data(final LineItem lineItem) {
            this.lineItem = lineItem;
        }
    }
}
