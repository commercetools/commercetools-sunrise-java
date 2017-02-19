package com.commercetools.sunrise.shoppingcart.cart.cartdetail.viewmodels;

import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.common.models.carts.CartBeanFactory;
import io.sphere.sdk.carts.Cart;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class CartDetailPageContentFactory extends PageContentFactory<CartDetailPageContent, Cart> {

    private final PageTitleResolver pageTitleResolver;
    private final CartBeanFactory cartBeanFactory;

    @Inject
    public CartDetailPageContentFactory(final PageTitleResolver pageTitleResolver, final CartBeanFactory cartBeanFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.cartBeanFactory = cartBeanFactory;
    }

    @Override
    protected CartDetailPageContent getViewModelInstance() {
        return new CartDetailPageContent();
    }

    @Override
    public final CartDetailPageContent create(@Nullable final Cart cart) {
        return super.create(cart);
    }

    protected final void initialize(final CartDetailPageContent model, @Nullable final Cart cart) {
        super.initialize(model, cart);
        fillCart(model, cart);
    }

    @Override
    protected void fillTitle(final CartDetailPageContent model, @Nullable final Cart cart) {
        model.setTitle(pageTitleResolver.getOrEmpty("checkout:cartDetailPage.title"));
    }

    protected void fillCart(final CartDetailPageContent model, @Nullable final Cart cart) {
        model.setCart(cartBeanFactory.create(cart));
    }
}
