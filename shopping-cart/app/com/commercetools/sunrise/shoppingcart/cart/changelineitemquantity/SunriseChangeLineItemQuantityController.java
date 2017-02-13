package com.commercetools.sunrise.shoppingcart.cart.changelineitemquantity;

import com.commercetools.sunrise.common.controllers.SunriseFrameworkFormController;
import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.WithCartFinder;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.ClientErrorException;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@IntroducingMultiControllerComponents(ChangeLineItemQuantityThemeLinksControllerComponent.class)
public abstract class SunriseChangeLineItemQuantityController<F extends ChangeLineItemQuantityFormData> extends SunriseFrameworkFormController implements WithFormFlow<F, Cart, Cart>, WithCartFinder {

    private final CartFinder cartFinder;
    private final CartDetailPageContentFactory cartDetailPageContentFactory;
    private final ChangeLineItemQuantityExecutor changeLineItemQuantityExecutor;

    protected SunriseChangeLineItemQuantityController(final TemplateRenderer templateRenderer, final RequestHookContext hookContext,
                                                      final CartFinder cartFinder, final FormFactory formFactory,
                                                      final CartDetailPageContentFactory cartDetailPageContentFactory,
                                                      final ChangeLineItemQuantityExecutor changeLineItemQuantityExecutor) {
        super(templateRenderer, hookContext, formFactory);
        this.cartFinder = cartFinder;
        this.cartDetailPageContentFactory = cartDetailPageContentFactory;
        this.changeLineItemQuantityExecutor = changeLineItemQuantityExecutor;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = new HashSet<>();
        frameworkTags.addAll(asList("cart", "manage-cart", "change-line-item-quantity"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "cart";
    }

    @Override
    public CartFinder getCartFinder() {
        return cartFinder;
    }

    @SunriseRoute("processChangeLineItemQuantityForm")
    public CompletionStage<Result> changeLineItemQuantity(final String languageTag) {
        return doRequest(() -> requireNonEmptyCart(this::processForm));
    }

    @Override
    public CompletionStage<Cart> executeAction(final Cart cart, final F formData) {
        return changeLineItemQuantityExecutor.apply(cart, formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Cart cart, final Form<F> form, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException);
        return showFormPageWithErrors(cart, form);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final F formData);

    @Override
    public PageContent createPageContent(final Cart cart, final Form<F> form) {
        return cartDetailPageContentFactory.create(cart);
    }

    @Override
    public void preFillFormData(final Cart cart, final F formData) {
        // Do not pre-fill anything
    }
}
