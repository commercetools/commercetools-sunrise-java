package com.commercetools.sunrise.shoppingcart.cart.addtocart;

import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.shoppingcart.CartCreator;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContent;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.ClientErrorException;
import play.data.Form;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Content;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@IntroducingMultiControllerComponents(AddProductToCartThemeLinksControllerComponent.class)
public abstract class SunriseAddProductToCartController<F extends AddProductToCartFormData> extends SunriseFrameworkController implements WithTemplateName, WithFormFlow<F, Cart, Cart> {

    private final CartCreator cartCreator;
    private final CartFinder cartFinder;
    private final AddProductToCartExecutor addProductToCartExecutor;
    private final CartDetailPageContentFactory cartDetailPageContentFactory;

    protected SunriseAddProductToCartController(final CartCreator cartCreator, final CartFinder cartFinder,
                                                final AddProductToCartExecutor addProductToCartExecutor,
                                                final CartDetailPageContentFactory cartDetailPageContentFactory) {
        this.cartCreator = cartCreator;
        this.cartFinder = cartFinder;
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

    @SuppressWarnings("unused")
    @SunriseRoute("processAddProductToCartForm")
    public CompletionStage<Result> addProductToCart(final String languageTag) {
        return doRequest(() -> getOrCreateCart(this::processForm));
    }

    @Override
    public CompletionStage<Cart> doAction(final F formData, final Cart cart) {
        return addProductToCartExecutor.apply(cart, formData);
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
    public void preFillFormData(final F formData, final Cart cart) {
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
