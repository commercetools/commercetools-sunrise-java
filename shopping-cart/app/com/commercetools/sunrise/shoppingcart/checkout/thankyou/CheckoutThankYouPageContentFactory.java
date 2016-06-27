package com.commercetools.sunrise.shoppingcart.checkout.thankyou;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.WithOverridablePageContent;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.shoppingcart.CartLikeBeanFactory;
import io.sphere.sdk.orders.Order;

import javax.inject.Inject;

public class CheckoutThankYouPageContentFactory extends PageContent implements WithOverridablePageContent<CheckoutThankYouPageContent> {
    @Inject
    protected CartLikeBeanFactory cartLikeBeanFactory;
    @Inject
    private I18nResolver i18nResolver;
    @Inject
    private UserContext userContext;

    public CheckoutThankYouPageContent create(final Order order) {
        final CheckoutThankYouPageContent pageContent = createPageContent();
        fill(pageContent, order);
        return pageContent;
    }

    protected void fill(final CheckoutThankYouPageContent pageContent, final Order order) {
        pageContent.setOrder(cartLikeBeanFactory.create(order));
        fillTitle(pageContent);
    }

    protected void fillTitle(final PageContent pageContent) {
        pageContent.setTitle(i18nResolver.getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:thankYouPage.title")));
    }

    @Override
    public CheckoutThankYouPageContent createPageContent() {
        return new CheckoutThankYouPageContent();
    }
}
