package com.commercetools.sunrise.shoppingcart.cart.changelineitemquantity;

import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContent;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkShoppingCartController;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.ClientErrorException;
import play.data.Form;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

@IntroducingMultiControllerComponents(ChangeLineItemQuantityThemeLinksControllerComponent.class)
public abstract class SunriseChangeLineItemQuantityController<F extends ChangeLineItemQuantityFormData> extends SunriseFrameworkShoppingCartController implements WithTemplateName, WithFormFlow<F, Cart, Cart> {

    private final CartDetailPageContentFactory cartDetailPageContentFactory;
    private final ChangeLineItemQuantityExecutor changeLineItemQuantityExecutor;

    protected SunriseChangeLineItemQuantityController(final CartFinder cartFinder, final CartDetailPageContentFactory cartDetailPageContentFactory,
                                                      final ChangeLineItemQuantityExecutor changeLineItemQuantityExecutor) {
        super(cartFinder);
        this.cartDetailPageContentFactory = cartDetailPageContentFactory;
        this.changeLineItemQuantityExecutor = changeLineItemQuantityExecutor;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = super.getFrameworkTags();
        frameworkTags.addAll(asList("cart", "manage-cart", "change-line-item-quantity"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "cart";
    }

    @SunriseRoute("processChangeLineItemQuantityForm")
    public CompletionStage<Result> changeLineItemQuantity(final String languageTag) {
        return doRequest(() -> requireNonEmptyCart(this::processForm));
    }

    @Override
    public CompletionStage<Cart> doAction(final F formData, final Cart cart) {
        return changeLineItemQuantityExecutor.apply(cart, formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<F> form, final Cart cart, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException);
        return asyncBadRequest(renderPage(form, cart, null));
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final F formData, final Cart oldCart, final Cart updatedCart);

    @Override
    public CompletionStage<Html> renderPage(final Form<F> form, final Cart cart, @Nullable final Cart updatedCart) {
        final CartDetailPageContent pageContent = cartDetailPageContentFactory.create(firstNonNull(updatedCart, cart));
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    @Override
    public void preFillFormData(final F formData, final Cart cart) {
        // Do not pre-fill anything
    }
}
