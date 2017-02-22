package com.commercetools.sunrise.common.models.carts;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.LineItem;

public abstract class AbstractLineItemViewModelFactory<T extends LineItemViewModel> extends ViewModelFactory<T, LineItem> {

    private final PriceFormatter priceFormatter;
    private final LineItemProductVariantViewModelFactory lineItemProductVariantViewModelFactory;

    protected AbstractLineItemViewModelFactory(final PriceFormatter priceFormatter, final LineItemProductVariantViewModelFactory lineItemProductVariantViewModelFactory) {
        this.priceFormatter = priceFormatter;
        this.lineItemProductVariantViewModelFactory = lineItemProductVariantViewModelFactory;
    }

    @Override
    protected void initialize(final T viewModel, final LineItem lineItem) {
        fillLineItemId(viewModel, lineItem);
        fillQuantity(viewModel, lineItem);
        fillVariant(viewModel, lineItem);
        fillTotalPrice(viewModel, lineItem);
    }

    protected void fillLineItemId(final T model, final LineItem lineItem) {
        model.setLineItemId(lineItem.getId());
    }

    protected void fillQuantity(final T model, final LineItem lineItem) {
        model.setQuantity(lineItem.getQuantity());
    }

    protected void fillVariant(final T model, final LineItem lineItem) {
        model.setVariant(lineItemProductVariantViewModelFactory.create(lineItem));
    }

    protected void fillTotalPrice(final T model, final LineItem lineItem) {
        model.setTotalPrice(priceFormatter.format(lineItem.getTotalPrice()));
    }
}
