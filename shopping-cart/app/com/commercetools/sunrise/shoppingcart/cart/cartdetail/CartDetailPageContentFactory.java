package com.commercetools.sunrise.shoppingcart.cart.cartdetail;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.shoppingcart.CartBeanFactory;

import javax.inject.Inject;

@RequestScoped
public class CartDetailPageContentFactory extends PageContentFactory<CartDetailPageContent, CartDetailControllerData> {

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
    public final CartDetailPageContent create(final CartDetailControllerData data) {
        return super.create(data);
    }

    protected final void initialize(final CartDetailPageContent model, final CartDetailControllerData data) {
        super.initialize(model, data);
        fillCart(model, data);
    }

    @Override
    protected void fillTitle(final CartDetailPageContent model, final CartDetailControllerData data) {
        model.setTitle(pageTitleResolver.getOrEmpty("checkout:cartDetailPage.title"));
    }

    protected void fillCart(final CartDetailPageContent model, final CartDetailControllerData data) {
        model.setCart(cartBeanFactory.create(data.getCart()));
    }
}
