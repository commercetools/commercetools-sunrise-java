package com.commercetools.sunrise.shoppingcart.cart.cartdetail;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.shoppingcart.CartBeanFactory;
import io.sphere.sdk.carts.Cart;

import javax.annotation.Nullable;
import javax.inject.Inject;

@RequestScoped
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
    public final CartDetailPageContent create(@Nullable final Cart data) {
        return super.create(data);
    }

    protected final void initialize(final CartDetailPageContent model, final Cart cart) {
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
