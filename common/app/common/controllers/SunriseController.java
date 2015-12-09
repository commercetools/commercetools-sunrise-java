package common.controllers;

import common.cms.CmsService;
import common.contexts.ProjectContext;
import common.contexts.RequestContext;
import common.contexts.UserContext;
import common.models.LocationSelector;
import common.models.MiniCart;
import common.models.NavMenuData;
import common.templates.TemplateService;
import common.utils.PriceFormatter;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTreeExtended;
import io.sphere.sdk.play.controllers.ShopController;
import io.sphere.sdk.play.metrics.MetricAction;
import play.Configuration;
import play.filters.csrf.AddCSRFToken;
import play.i18n.Lang;
import play.i18n.Messages;
import play.mvc.Http;
import play.mvc.With;
import purchase.CartSessionUtils;

import javax.annotation.Nullable;
import javax.money.Monetary;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.neovisionaries.i18n.CountryCode.DE;
import static java.util.stream.Collectors.toList;

/**
 * An application specific controller.
 * Since we want to show a standard web shop it contains categories.
 */
@With(MetricAction.class)
@AddCSRFToken
public abstract class SunriseController extends ShopController {
    private final ControllerDependency controllerDependency;
    private final Optional<String> saleCategoryExtId;
    private final Optional<String> categoryNewExtId;

    protected SunriseController(final ControllerDependency controllerDependency) {
        super(controllerDependency.sphere());
        this.controllerDependency = controllerDependency;
        this.saleCategoryExtId = Optional.ofNullable(controllerDependency.configuration().getString("common.saleCategoryExternalId"));
        this.categoryNewExtId = Optional.ofNullable(controllerDependency.configuration().getString("common.newCategoryExternalId"));
    }

    protected final CategoryTreeExtended categoryTree() {
        return controllerDependency.categoryTree();
    }

    protected TemplateService templateService() {
        return controllerDependency.templateService();
    }

    protected final CmsService cmsService() {
        return controllerDependency.cmsService();
    }

    protected final Configuration configuration() {
        return controllerDependency.configuration();
    }

    protected final ReverseRouter reverseRouter() {
        return controllerDependency.getReverseRouter();
    }

    protected ProjectContext projectContext() {
        return controllerDependency.projectContext();
    }

    protected final SunrisePageData pageData(final UserContext userContext, final PageContent content, final Http.Context ctx) {
        final PageHeader pageHeader = new PageHeader(content.getAdditionalTitle());
        pageHeader.setLocation(new LocationSelector(projectContext(), userContext));
        pageHeader.setNavMenu(new NavMenuData(categoryTree(), userContext, reverseRouter(), saleCategoryExtId.orElse(null)));
        pageHeader.setMiniCart(new MiniCart(CartSessionUtils.getCartItemCount(session())));
        pageHeader.setCustomerServiceNumber(configuration().getString("checkout.customerServiceNumber"));
        return new SunrisePageData(pageHeader, new PageFooter(), content, getPageMeta(ctx, userContext));
    }

    private PageMeta getPageMeta(final Http.Context ctx, final UserContext userContext) {
        final PageMeta pageMeta = new PageMeta();
        pageMeta.setAssetsPath(reverseRouter().designAssets("").url());;
        pageMeta.setBagQuantityOptions(IntStream.rangeClosed(1, 9).boxed().collect(toList()));
        pageMeta.setCsrfToken(SunriseController.getCsrfToken(ctx.session()));
        final String language = userContext.locale().getLanguage();
        pageMeta.addHalLink(reverseRouter().showCart(language), "cart")
                .addHalLink(reverseRouter().showCheckoutShippingForm(language), "checkout", "editShippingAddress", "editBillingAddress", "editShippingMethod")
                .addHalLink(reverseRouter().showCheckoutPaymentForm(language), "editPaymentInfo")
                .addHalLink(reverseRouter().home(language), "continueShopping", "home")
                .addHalLink(reverseRouter().productToCartForm(language), "addToCart")
                .addHalLink(reverseRouter().processCheckoutShippingForm(language), "checkoutAddressesSubmit")
                .addHalLink(reverseRouter().processCheckoutPaymentForm(language), "checkoutPaymentSubmit")
                .addHalLink(reverseRouter().processCheckoutConfirmationForm(language), "checkoutConfirmationSubmit")
                .addHalLink(reverseRouter().processDeleteLineItem(language), "deleteLineItem")
                .addHalLink(reverseRouter().processChangeLineItemQuantity(language), "changeLineItem")
                .addHalLinkOfHrefAndRel(ctx.request().uri(), "self");
        return pageMeta;
    }

    protected final Messages messages(final UserContext userContext) {
        final Lang lang = Lang.forCode(userContext.locale().toLanguageTag());
        return new Messages(lang, controllerDependency.messagesApi());
    }

    protected PriceFormatter priceFormatter(final UserContext userContext) {
        return PriceFormatter.of(userContext.locale());
    }

    protected UserContext userContext(final String language) {
        final ArrayList<Locale> locales = new ArrayList<>();
        locales.add(Locale.forLanguageTag(language));
        locales.addAll(request().acceptLanguages().stream()
                .map(lang -> Locale.forLanguageTag(lang.code()))
                .collect(toList()));
        return UserContext.of(DE, locales, ZoneId.of("Europe/Berlin"), Monetary.getCurrency("EUR"));
    }

    protected RequestContext requestContext() {
        return RequestContext.of(request().queryString(), request().path());
    }

    protected Optional<Category> newCategory() {
        return categoryNewExtId.flatMap(extId -> categoryTree().findByExternalId(extId));
    }

    @Nullable
    public static String getCsrfToken(final Http.Session session) {
        return session.get("csrfToken");
    }
}
