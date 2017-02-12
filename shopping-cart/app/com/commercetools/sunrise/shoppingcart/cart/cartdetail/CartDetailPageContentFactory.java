package com.commercetools.sunrise.shoppingcart.cart.cartdetail;

import com.commercetools.sunrise.common.models.FormPageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.shoppingcart.CartBeanFactory;
import com.commercetools.sunrise.shoppingcart.cart.addtocart.AddProductToCartFormData;
import io.sphere.sdk.carts.Cart;
import play.data.Form;

import javax.inject.Inject;

public class CartDetailPageContentFactory extends FormPageContentFactory<CartDetailPageContent, Cart, AddProductToCartFormData> {

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
    public final CartDetailPageContent create(final Cart cart, final Form<? extends AddProductToCartFormData> form) {
        return super.create(cart, form);
    }

    protected final void initialize(final CartDetailPageContent model, final Cart cart, final Form<? extends AddProductToCartFormData> form) {
        super.initialize(model, cart, form);
        fillCart(model, cart, form);
    }

    @Override
    protected void fillTitle(final CartDetailPageContent model, final Cart cart, final Form<? extends AddProductToCartFormData> form) {
        model.setTitle(pageTitleResolver.getOrEmpty("checkout:cartDetailPage.title"));
    }

    protected void fillCart(final CartDetailPageContent model, final Cart cart, final Form<? extends AddProductToCartFormData> form) {
        model.setCart(cartBeanFactory.create(cart));
    }
}
