package com.commercetools.sunrise.framework.cart.cartdetail.viewmodels;

import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.common.models.carts.CartViewModelFactory;
import io.sphere.sdk.carts.Cart;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class CartDetailPageContentFactory extends PageContentFactory<CartDetailPageContent, Cart> {

    private final PageTitleResolver pageTitleResolver;
    private final CartViewModelFactory cartViewModelFactory;

    @Inject
    public CartDetailPageContentFactory(final PageTitleResolver pageTitleResolver, final CartViewModelFactory cartViewModelFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.cartViewModelFactory = cartViewModelFactory;
    }

    @Override
    protected CartDetailPageContent getViewModelInstance() {
        return new CartDetailPageContent();
    }

    @Override
    public final CartDetailPageContent create(@Nullable final Cart cart) {
        return super.create(cart);
    }

    protected final void initialize(final CartDetailPageContent viewModel, @Nullable final Cart cart) {
        super.initialize(viewModel, cart);
        fillCart(viewModel, cart);
    }

    @Override
    protected void fillTitle(final CartDetailPageContent viewModel, @Nullable final Cart cart) {
        viewModel.setTitle(pageTitleResolver.getOrEmpty("checkout:cartDetailPage.title"));
    }

    protected void fillCart(final CartDetailPageContent viewModel, @Nullable final Cart cart) {
        viewModel.setCart(cartViewModelFactory.create(cart));
    }
}
