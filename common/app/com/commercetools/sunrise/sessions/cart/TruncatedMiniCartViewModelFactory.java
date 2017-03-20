package com.commercetools.sunrise.sessions.cart;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.viewmodels.formatters.PriceFormatter;
import com.commercetools.sunrise.framework.viewmodels.content.carts.LineItemViewModel;
import com.commercetools.sunrise.framework.viewmodels.content.carts.LineItemViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.content.carts.MiniCartViewModel;
import com.commercetools.sunrise.framework.viewmodels.content.carts.MiniCartViewModelFactory;
import io.sphere.sdk.carts.Cart;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.money.CurrencyUnit;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class TruncatedMiniCartViewModelFactory extends MiniCartViewModelFactory {

    private static final int LINE_ITEMS_LIMIT = 5;

    @Inject
    public TruncatedMiniCartViewModelFactory(final CurrencyUnit currency, final PriceFormatter priceFormatter, final LineItemViewModelFactory lineItemViewModelFactory) {
        super(currency, priceFormatter, lineItemViewModelFactory);
    }

    @Override
    protected void fillLineItems(final MiniCartViewModel viewModel, @Nullable final Cart cart) {
        super.fillLineItems(viewModel, cart);
        if (viewModel.getLineItems() != null) {
            viewModel.getLineItems().setList(truncateLineItems(viewModel.getLineItems().getList(), LINE_ITEMS_LIMIT));
        }
    }

    /**
     * Limits the mini cart line item entries to avoid a too large mini cart that won't fit in session.
     * @param lineItemList the line items in cart
     * @param limit the maximum amount of line items to return
     * @return the line items in cart limited up to the given amount
     */
    private List<LineItemViewModel> truncateLineItems(final List<LineItemViewModel> lineItemList, final int limit) {
        return lineItemList.stream()
                .limit(limit)
                .collect(toList());
    }
}
