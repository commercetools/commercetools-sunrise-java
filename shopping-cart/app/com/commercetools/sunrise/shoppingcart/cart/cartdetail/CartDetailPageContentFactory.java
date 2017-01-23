package com.commercetools.sunrise.shoppingcart.cart.cartdetail;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.shoppingcart.CartBeanFactory;
import io.sphere.sdk.carts.Cart;

import javax.annotation.Nullable;
import javax.inject.Inject;

@RequestScoped
public class CartDetailPageContentFactory extends PageContentFactory {

    private final PageTitleResolver pageTitleResolver;
    private final CartBeanFactory cartBeanFactory;

    @Inject
    public CartDetailPageContentFactory(final PageTitleResolver pageTitleResolver, final CartBeanFactory cartBeanFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.cartBeanFactory = cartBeanFactory;
    }

    public CartDetailPageContent create(@Nullable final Cart cart) {
        final CartDetailPageContent bean = new CartDetailPageContent();
        initialize(bean, cart);
        return bean;
    }

    protected final void initialize(final CartDetailPageContent bean, @Nullable final Cart cart) {
        fillCart(bean, cart);
        fillTitle(bean, cart);
    }

    protected void fillCart(final CartDetailPageContent bean, @Nullable final Cart cart) {
        bean.setCart(cartBeanFactory.create(cart));
    }

    protected void fillTitle(final CartDetailPageContent bean, @Nullable final Cart cart) {
        bean.setTitle(pageTitleResolver.getOrEmpty("checkout:cartDetailPage.title"));
    }
}
