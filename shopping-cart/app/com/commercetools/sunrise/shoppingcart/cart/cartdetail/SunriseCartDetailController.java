package com.commercetools.sunrise.shoppingcart.cart.cartdetail;

import com.commercetools.sunrise.common.controllers.SunriseTemplateController;
import com.commercetools.sunrise.common.controllers.WithQueryFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.WithRequiredCart;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import io.sphere.sdk.carts.Cart;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public abstract class SunriseCartDetailController extends SunriseTemplateController implements WithQueryFlow<Cart>, WithRequiredCart {

    private final CartFinder cartFinder;
    private final CartDetailPageContentFactory cartDetailPageContentFactory;

    protected SunriseCartDetailController(final ComponentRegistry componentRegistry, final TemplateRenderer templateRenderer,
                                          final CartFinder cartFinder, final CartDetailPageContentFactory cartDetailPageContentFactory) {
        super(componentRegistry, templateRenderer);
        this.cartFinder = cartFinder;
        this.cartDetailPageContentFactory = cartDetailPageContentFactory;
    }

    @Inject
    private void registerThemeLinks(final CartDetailThemeLinksControllerComponent themeLinksControllerComponent) {
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

    @SunriseRoute("showCart")
    public CompletionStage<Result> show(final String languageTag) {
        return requireCart(this::showPage);
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
