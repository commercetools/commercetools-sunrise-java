package purchase;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.models.ProductDataConfig;
import common.pages.SunrisePageData;
import io.sphere.sdk.carts.Cart;
import play.data.DynamicForm;
import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Messages;
import play.libs.F;
import play.mvc.Result;

import javax.inject.Inject;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class CheckoutShippingController extends CartController {


    private final ShippingMethods shippingMethods;
    private final ProductDataConfig productDataConfig;

    @Inject
    public CheckoutShippingController(final ControllerDependency controllerDependency, final ShippingMethods shippingMethods, final ProductDataConfig productDataConfig) {
        super(controllerDependency);
        this.shippingMethods = shippingMethods;
        this.productDataConfig = productDataConfig;
    }

    public F.Promise<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final F.Promise<Cart> cartPromise = getOrCreateCart(userContext, session());
        return cartPromise.map(cart -> {
            final Messages messages = messages(userContext);
            final CheckoutShippingContent content = new CheckoutShippingContent(cart, messages, configuration(), reverseRouter(), userContext, flash(), getCsrfToken(), shippingMethods, productDataConfig);
            final SunrisePageData pageData = pageData(userContext, content);
            return ok(templateService().renderToHtml("checkout-shipping", pageData, userContext.locales()));
        });
    }

    @RequireCSRFCheck
    public F.Promise<Result> process(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final F.Promise<Cart> cartPromise = getOrCreateCart(userContext, session());
        return cartPromise.map(cart -> {
            final Form<CheckoutShippingFormData> filledForm = Form.form(CheckoutShippingFormData.class).bindFromRequest(request());
            if (filledForm.hasErrors()) {
                final Messages messages = messages(userContext);
                final CheckoutShippingContent content = new CheckoutShippingContent(filledForm, cart, messages, configuration(), reverseRouter(), userContext, flash(), getCsrfToken(), shippingMethods, productDataConfig);
                final SunrisePageData pageData = pageData(userContext, content);
                return badRequest(templateService().renderToHtml("checkout-shipping", pageData, userContext.locales()));
            } else {
                return TODO;
            }
        });
    }
}