package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.ProductVariantBeanFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.LineItem;

import javax.inject.Inject;

@RequestScoped
public class LineItemBeanFactory extends AbstractLineItemBeanFactory<LineItemBean> {

    @Inject
    public LineItemBeanFactory(final PriceFormatter priceFormatter, final ProductVariantBeanFactory productVariantBeanFactory) {
        super(priceFormatter, productVariantBeanFactory);
    }

    public final LineItemBean create(final LineItem lineItem) {
        final Data data = new Data(lineItem);
        return initializedViewModel(data);
    }

    @Override
    protected LineItemBean getViewModelInstance() {
        return new LineItemBean();
    }

    @Override
    protected final void initialize(final LineItemBean bean, final Data data) {
        super.initialize(bean, data);
    }
}
