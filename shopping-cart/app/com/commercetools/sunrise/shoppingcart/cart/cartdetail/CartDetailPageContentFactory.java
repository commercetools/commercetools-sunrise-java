package com.commercetools.sunrise.shoppingcart.cart.cartdetail;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.WithOverridablePageContent;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.shoppingcart.CartLikeBeanFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Base;

import javax.inject.Inject;

public class CartDetailPageContentFactory extends Base implements WithOverridablePageContent<CartDetailPageContent> {
    @Inject
    private CartLikeBeanFactory cartLikeBeanFactory;
    @Inject
    private I18nResolver i18nResolver;
    @Inject
    private UserContext userContext;

    public CartDetailPageContent create(final Cart cart) {
        final CartDetailPageContent pageContent = createPageContent();
        pageContent.setCart(cartLikeBeanFactory.create(cart));
        fillTitle(pageContent);
        return pageContent;
    }

    protected void fillTitle(final PageContent pageContent) {
        pageContent.setTitle(i18nResolver.getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:cartDetailPage.title")));
    }

    @Override
    public CartDetailPageContent createPageContent() {
        return new CartDetailPageContent();
    }
}
