package com.commercetools.sunrise.shoppingcart.cart.removelineitem;

import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.SunriseFrameworkShoppingCartController;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContent;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.ClientErrorException;
import play.data.Form;
import play.mvc.Result;
import play.twirl.api.Content;

import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@IntroducingMultiControllerComponents(RemoveLineItemThemeLinksControllerComponent.class)
public abstract class SunriseRemoveLineItemController<F extends RemoveLineItemFormData> extends SunriseFrameworkShoppingCartController implements WithTemplateName, WithFormFlow<F, Cart, Cart> {

    private final RemoveLineItemExecutor removeLineItemExecutor;
    private final CartDetailPageContentFactory cartDetailPageContentFactory;

    protected SunriseRemoveLineItemController(final CartFinder cartFinder, final RemoveLineItemExecutor removeLineItemExecutor,
                                              final CartDetailPageContentFactory cartDetailPageContentFactory) {
        super(cartFinder);
        this.removeLineItemExecutor = removeLineItemExecutor;
        this.cartDetailPageContentFactory = cartDetailPageContentFactory;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = super.getFrameworkTags();
        frameworkTags.addAll(asList("cart", "manage-cart", "remove-line-item-from-cart"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "cart";
    }

    @SunriseRoute("processDeleteLineItemForm")
    public CompletionStage<Result> removeLineItem(final String languageTag) {
        return doRequest(() -> requireNonEmptyCart(this::processForm));
    }

    @Override
    public CompletionStage<Cart> doAction(final F formData, final Cart cart) {
        return removeLineItemExecutor.apply(cart, formData);
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
        final CartDetailPageContent pageContent = cartDetailPageContentFactory.create(cart);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    @Override
    public void preFillFormData(final F formData, final Cart input) {
        // Do nothing
    }
}
