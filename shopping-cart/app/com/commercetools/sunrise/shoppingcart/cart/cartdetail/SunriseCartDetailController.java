package com.commercetools.sunrise.shoppingcart.cart.cartdetail;

import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.controllers.WithQueryFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.WithCartFinder;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import io.sphere.sdk.carts.Cart;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@IntroducingMultiControllerComponents(CartDetailThemeLinksControllerComponent.class)
public abstract class SunriseCartDetailController extends SunriseFrameworkController implements WithQueryFlow<Cart>, WithCartFinder {

    private final CartFinder cartFinder;
    private final CartDetailPageContentFactory cartDetailPageContentFactory;

    protected SunriseCartDetailController(final TemplateRenderer templateRenderer, final RequestHookContext hookContext,
                                          final CartFinder cartFinder, final CartDetailPageContentFactory cartDetailPageContentFactory) {
        super(templateRenderer, hookContext);
        this.cartFinder = cartFinder;
        this.cartDetailPageContentFactory = cartDetailPageContentFactory;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = new HashSet<>();
        frameworkTags.addAll(asList("cart", "cart-detail"));
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

    @SunriseRoute("showCart")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> requireNonEmptyCart(this::showPage));
    }

    @Override
    public CompletionStage<Result> handleNotFoundCart() {
        return okResultWithPageContent(cartDetailPageContentFactory.create(null));
    }

    @Override
    public PageContent createPageContent(final Cart cart) {
        return cartDetailPageContentFactory.create(cart);
    }
}
