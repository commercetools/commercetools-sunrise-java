package com.commercetools.sunrise.common.models.carts;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.LineItem;

import javax.inject.Inject;

@RequestScoped
public class LineItemViewModelFactory extends AbstractLineItemViewModelFactory<LineItemViewModel> {

    @Inject
    public LineItemViewModelFactory(final PriceFormatter priceFormatter, final LineItemProductVariantViewModelFactory lineItemProductVariantViewModelFactory) {
        super(priceFormatter, lineItemProductVariantViewModelFactory);
    }

    @Override
    protected LineItemViewModel getViewModelInstance() {
        return new LineItemViewModel();
    }

    @Override
    public final LineItemViewModel create(final LineItem lineItem) {
        return super.create(lineItem);
    }

    @Override
    protected final void initialize(final LineItemViewModel viewModel, final LineItem lineItem) {
        super.initialize(viewModel, lineItem);
    }
}
