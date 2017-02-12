package com.commercetools.sunrise.shoppingcart.cart.cartdetail;

import com.commercetools.sunrise.common.controllers.WithFetchFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContent;
import com.commercetools.sunrise.shoppingcart.cart.cartdetail.view.CartDetailPageContentFactory;
import com.commercetools.sunrise.shoppingcart.SunriseFrameworkShoppingCartController;
import io.sphere.sdk.carts.Cart;
import play.mvc.Result;
import play.twirl.api.Html;

import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@IntroducingMultiControllerComponents(CartDetailThemeLinksControllerComponent.class)
public abstract class SunriseCartDetailController extends SunriseFrameworkShoppingCartController implements WithTemplateName, WithFetchFlow<Cart> {

    private final CartDetailPageContentFactory cartDetailPageContentFactory;

    protected SunriseCartDetailController(final CartFinder cartFinder, final CartDetailPageContentFactory cartDetailPageContentFactory) {
        super(cartFinder);
        this.cartDetailPageContentFactory = cartDetailPageContentFactory;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = super.getFrameworkTags();
        frameworkTags.addAll(asList("cart", "cart-detail"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "cart";
    }

    @SunriseRoute("showCart")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> requireNonEmptyCart(this::showPage));
    }

    @Override
    protected CompletionStage<Result> handleNotFoundCart() {
        final CartDetailPageContent pageContent = cartDetailPageContentFactory.create(null);
        return asyncOk(renderPageWithTemplate(pageContent, getTemplateName()));
    }

    @Override
    public CompletionStage<Html> renderPage(final Cart cart) {
        final CartDetailPageContent pageContent = cartDetailPageContentFactory.create(cart);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }
}
