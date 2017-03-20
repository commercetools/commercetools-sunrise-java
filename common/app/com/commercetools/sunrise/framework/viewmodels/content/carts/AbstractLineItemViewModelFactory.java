package com.commercetools.sunrise.framework.viewmodels.content.carts;

import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.formatters.PriceFormatter;
import io.sphere.sdk.carts.LineItem;

public abstract class AbstractLineItemViewModelFactory<T extends LineItemViewModel> extends SimpleViewModelFactory<T, LineItem> {

    private final PriceFormatter priceFormatter;
    private final LineItemProductVariantViewModelFactory lineItemProductVariantViewModelFactory;

    protected AbstractLineItemViewModelFactory(final PriceFormatter priceFormatter, final LineItemProductVariantViewModelFactory lineItemProductVariantViewModelFactory) {
        this.priceFormatter = priceFormatter;
        this.lineItemProductVariantViewModelFactory = lineItemProductVariantViewModelFactory;
    }

    protected final PriceFormatter getPriceFormatter() {
        return priceFormatter;
    }

    protected final LineItemProductVariantViewModelFactory getLineItemProductVariantViewModelFactory() {
        return lineItemProductVariantViewModelFactory;
    }

    @Override
    protected void initialize(final T viewModel, final LineItem lineItem) {
        fillLineItemId(viewModel, lineItem);
        fillQuantity(viewModel, lineItem);
        fillVariant(viewModel, lineItem);
        fillTotalPrice(viewModel, lineItem);
    }

    protected void fillLineItemId(final T viewModel, final LineItem lineItem) {
        viewModel.setLineItemId(lineItem.getId());
    }

    protected void fillQuantity(final T viewModel, final LineItem lineItem) {
        viewModel.setQuantity(lineItem.getQuantity());
    }

    protected void fillVariant(final T viewModel, final LineItem lineItem) {
        viewModel.setVariant(lineItemProductVariantViewModelFactory.create(lineItem));
    }

    protected void fillTotalPrice(final T viewModel, final LineItem lineItem) {
        viewModel.setTotalPrice(priceFormatter.format(lineItem.getTotalPrice()));
    }
}
