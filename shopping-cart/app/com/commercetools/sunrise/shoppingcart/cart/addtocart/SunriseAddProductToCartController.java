package com.commercetools.sunrise.shoppingcart.cart.addtocart;

import com.commercetools.sunrise.common.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.common.controllers.WithTemplateFormFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import com.commercetools.sunrise.shoppingcart.CartCreator;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.WithRequiredCart;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.ClientErrorException;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public abstract class SunriseAddProductToCartController<F extends AddProductToCartFormData> extends SunriseTemplateFormController implements WithTemplateFormFlow<F, Cart, Cart>, WithRequiredCart {

    private final CartFinder cartFinder;
    private final CartCreator cartCreator;
    private final AddProductToCartExecutor addProductToCartExecutor;
    private final CartDetailPageContentFactory cartDetailPageContentFactory;

    protected SunriseAddProductToCartController(final ComponentRegistry componentRegistry, final TemplateRenderer templateRenderer,
                                                final FormFactory formFactory, final CartFinder cartFinder, final CartCreator cartCreator,
                                                final AddProductToCartExecutor addProductToCartExecutor,
                                                final CartDetailPageContentFactory cartDetailPageContentFactory) {
        super(componentRegistry, templateRenderer, formFactory);
        this.cartFinder = cartFinder;
        this.cartCreator = cartCreator;
        this.addProductToCartExecutor = addProductToCartExecutor;
        this.cartDetailPageContentFactory = cartDetailPageContentFactory;
    }

    @Inject
    private void registerThemeLinks(final AddProductToCartThemeLinksControllerComponent themeLinksControllerComponent) {
        register(themeLinksControllerComponent);
    }

    @Override
    public String getTemplateName() {
        return "cart";
    }

    @Override
    public CartFinder getCartFinder() {
        return cartFinder;
    }

    public CartCreator getCartCreator() {
        return cartCreator;
    }

    @SuppressWarnings("unused")
    @SunriseRoute("processAddProductToCartForm")
    public CompletionStage<Result> addProductToCart(final String languageTag) {
        return requireCart(this::processForm);
    }

    @Override
    public CompletionStage<Cart> executeAction(final Cart cart, final F formData) {
        return addProductToCartExecutor.apply(cart, formData);
    }

    @Override
    public CompletionStage<Result> handleNotFoundCart() {
        return cartCreator.get()
                .thenComposeAsync(this::processForm, HttpExecution.defaultContext());
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
        // Do not pre-fill with anything
    }
}
