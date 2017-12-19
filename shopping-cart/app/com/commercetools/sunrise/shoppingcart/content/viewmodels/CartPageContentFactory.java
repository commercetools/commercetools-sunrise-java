package com.commercetools.sunrise.shoppingcart.content.viewmodels;

import com.commercetools.sunrise.core.viewmodels.content.PageContentFactory;
import com.commercetools.sunrise.shoppingcart.adddiscountcode.AddDiscountCodeFormData;
import io.sphere.sdk.carts.Cart;
import play.data.Form;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class CartPageContentFactory extends PageContentFactory<CartPageContent, Cart> {

    @Inject
    public CartPageContentFactory() {
    }

    @Override
    protected CartPageContent newViewModelInstance(@Nullable final Cart cart) {
        return new CartPageContent();
    }

    @Override
    public final CartPageContent create(@Nullable final Cart cart) {
        return super.create(cart);
    }

    public final CartPageContent create(@Nullable final Cart cart, final Form<? extends AddDiscountCodeFormData> form) {
        final CartPageContent cartPageContent = super.create(cart);
        return cartPageContent;
    }

    protected final void initialize(final CartPageContent viewModel, @Nullable final Cart cart) {
        super.initialize(viewModel, cart);
        fillCart(viewModel, cart);
    }

    @Override
    protected void fillTitle(final CartPageContent viewModel, @Nullable final Cart cart) {
    }

    protected void fillCart(final CartPageContent viewModel, @Nullable final Cart cart) {
        viewModel.setCart(cart);
    }
}
