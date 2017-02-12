package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.SunriseFrameworkShoppingCartController;
import com.commercetools.sunrise.shoppingcart.checkout.address.view.CheckoutAddressPageContent;
import com.commercetools.sunrise.shoppingcart.checkout.address.view.CheckoutAddressPageContentFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.ClientErrorException;
import play.data.Form;
import play.mvc.Result;
import play.twirl.api.Content;

import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@IntroducingMultiControllerComponents(CheckoutAddressThemeLinksControllerComponent.class)
public abstract class SunriseCheckoutAddressController<F extends CheckoutAddressFormData> extends SunriseFrameworkShoppingCartController implements WithTemplateName, WithFormFlow<F, Cart, Cart> {

    private final CheckoutAddressExecutor checkoutAddressExecutor;
    private final CheckoutAddressPageContentFactory checkoutAddressPageContentFactory;

    protected SunriseCheckoutAddressController(final CartFinder cartFinder, final CheckoutAddressExecutor checkoutAddressExecutor,
                                               final CheckoutAddressPageContentFactory checkoutAddressPageContentFactory) {
        super(cartFinder);
        this.checkoutAddressExecutor = checkoutAddressExecutor;
        this.checkoutAddressPageContentFactory = checkoutAddressPageContentFactory;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = super.getFrameworkTags();
        frameworkTags.addAll(asList("checkout", "checkout-address"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "checkout-address";
    }

    @SunriseRoute("checkoutAddressesPageCall")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> requireNonEmptyCart(this::showFormPage));
    }

    @SuppressWarnings("unused")
    @SunriseRoute("checkoutAddressesProcessFormCall")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> requireNonEmptyCart(this::processForm));
    }

    @Override
    public CompletionStage<Cart> doAction(final F formData, final Cart cart) {
        return checkoutAddressExecutor.apply(cart, formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<F> form, final Cart cart, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException);
        return asyncBadRequest(renderPage(form, cart));
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final F formData, final Cart oldCart, final Cart updatedCart);

    @Override
    public CompletionStage<Content> renderPage(final Form<F> form, final Cart cart) {
        final CheckoutAddressPageContent pageContent = checkoutAddressPageContentFactory.create(cart, form);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    @Override
    public Form<F> createForm() {
        return isBillingDifferent()
                ? formFactory().form(getFormDataClass(), BillingAddressDifferentToShippingAddressGroup.class)
                : formFactory().form(getFormDataClass());
    }

    @Override
    public void preFillFormData(final F formData, final Cart cart) {
        formData.setData(cart);
    }

    protected boolean isBillingDifferent() {
        final String flagFieldName = "billingAddressDifferentToBillingAddress";
        final String fieldValue = formFactory().form().bindFromRequest().get(flagFieldName);
        return "true".equals(fieldValue);
    }
}