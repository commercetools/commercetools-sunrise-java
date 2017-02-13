package com.commercetools.sunrise.shoppingcart.cart.addtocart;

import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.shoppingcart.CartCreator;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.ClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@IntroducingMultiControllerComponents(AddProductToCartThemeLinksControllerComponent.class)
public abstract class SunriseAddProductToCartController<F extends AddProductToCartFormData> extends SunriseFrameworkController implements WithFormFlow<F, Cart, Cart> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CartCreator cartCreator;
    private final CartFinder cartFinder;
    private final FormFactory formFactory;
    private final AddProductToCartExecutor addProductToCartExecutor;
    private final CartDetailPageContentFactory cartDetailPageContentFactory;

    protected SunriseAddProductToCartController(final TemplateRenderer templateRenderer, final RequestHookContext hookContext,
                                                final CartCreator cartCreator, final CartFinder cartFinder, final FormFactory formFactory,
                                                final AddProductToCartExecutor addProductToCartExecutor,
                                                final CartDetailPageContentFactory cartDetailPageContentFactory) {
        super(templateRenderer, hookContext);
        this.cartCreator = cartCreator;
        this.cartFinder = cartFinder;
        this.formFactory = formFactory;
        this.addProductToCartExecutor = addProductToCartExecutor;
        this.cartDetailPageContentFactory = cartDetailPageContentFactory;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = new HashSet<>();
        frameworkTags.addAll(asList("shopping-cart", "cart", "add-line-item-to-cart"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "cart";
    }

    @Override
    public FormFactory getFormFactory() {
        return formFactory;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @SuppressWarnings("unused")
    @SunriseRoute("processAddProductToCartForm")
    public CompletionStage<Result> addProductToCart(final String languageTag) {
        return doRequest(() -> getOrCreateCart(this::processForm));
    }

    @Override
    public CompletionStage<Cart> executeAction(final Cart cart, final F formData) {
        return addProductToCartExecutor.apply(cart, formData);
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

    protected final CompletionStage<Result> getOrCreateCart(final Function<Cart, CompletionStage<Result>> nextAction) {
        return cartFinder.get()
                .thenComposeAsync(cartOpt -> cartOpt
                                .map(cart -> (CompletionStage<Cart>) completedFuture(cart))
                                .orElseGet(cartCreator)
                                .thenComposeAsync(nextAction, HttpExecution.defaultContext()),
                        HttpExecution.defaultContext());
    }
}
