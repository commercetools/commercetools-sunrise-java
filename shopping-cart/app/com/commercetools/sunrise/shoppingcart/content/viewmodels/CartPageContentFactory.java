package com.commercetools.sunrise.shoppingcart.content.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.PageTitleResolver;
import com.commercetools.sunrise.framework.viewmodels.content.PageContentFactory;
import com.commercetools.sunrise.framework.viewmodels.content.carts.CartViewModelFactory;
import io.sphere.sdk.carts.Cart;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class CartPageContentFactory extends PageContentFactory<CartPageContent, Cart> {

    private final PageTitleResolver pageTitleResolver;
    private final CartViewModelFactory cartViewModelFactory;

    @Inject
    public CartPageContentFactory(final PageTitleResolver pageTitleResolver, final CartViewModelFactory cartViewModelFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.cartViewModelFactory = cartViewModelFactory;
    }

    protected final PageTitleResolver getPageTitleResolver() {
        return pageTitleResolver;
    }

    protected final CartViewModelFactory getCartViewModelFactory() {
        return cartViewModelFactory;
    }

    @Override
    protected CartPageContent newViewModelInstance(@Nullable final Cart cart) {
        return new CartPageContent();
    }

    @Override
    public final CartPageContent create(@Nullable final Cart cart) {
        return super.create(cart);
    }

    protected final void initialize(final CartPageContent viewModel, @Nullable final Cart cart) {
        super.initialize(viewModel, cart);
        fillCart(viewModel, cart);
    }

    @Override
    protected void fillTitle(final CartPageContent viewModel, @Nullable final Cart cart) {
        viewModel.setTitle(pageTitleResolver.getOrEmpty("checkout:cartDetailPage.title"));
    }

    protected void fillCart(final CartPageContent viewModel, @Nullable final Cart cart) {
        viewModel.setCart(cartViewModelFactory.create(cart));
    }
}
