package com.commercetools.sunrise.shoppingcart.cart.cartdetail;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.shoppingcart.CartBeanFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Base;

import javax.inject.Inject;

public class CartDetailPageContentFactory extends Base {
    @Inject
    private CartBeanFactory cartLikeBeanFactory;
    @Inject
    private I18nResolver i18nResolver;
    @Inject
    private I18nIdentifierFactory i18nIdentifierFactory;
    @Inject
    private UserContext userContext;

    public CartDetailPageContent create(final Cart cart) {
        final CartDetailPageContent bean = new CartDetailPageContent();
        initialize(bean, cart);
        return bean;
    }

    protected final void initialize(final CartDetailPageContent bean, final Cart cart) {
        fillCart(bean, cart);
        fillTitle(bean);
    }

    protected void fillCart(final CartDetailPageContent bean, final Cart cart) {
        bean.setCart(cartLikeBeanFactory.create(cart));
    }

    protected void fillTitle(final PageContent bean) {
        final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create("checkout:cartDetailPage.title");
        bean.setTitle(i18nResolver.getOrEmpty(userContext.locales(), i18nIdentifier));
    }
}
