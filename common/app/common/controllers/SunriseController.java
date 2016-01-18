package common.controllers;

import com.neovisionaries.i18n.CountryCode;
import common.cms.CmsService;
import common.contexts.ProjectContext;
import common.contexts.RequestContext;
import common.contexts.UserContext;
import common.i18n.I18nResolver;
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
import play.mvc.Http;
import play.mvc.With;
import shoppingcart.CartSessionUtils;

import javax.annotation.Nullable;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * An application specific controller.
 * Since we want to show a standard web shop it contains categories.
 */
@With(MetricAction.class)
@AddCSRFToken
public abstract class SunriseController extends ShopController {
    public static final String SESSION_COUNTRY = "countryCode";
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

    protected final I18nResolver i18nResolver() {
        return controllerDependency.i18nResolver();
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
        pageHeader.setMiniCart(CartSessionUtils.getMiniCart(session()));
        pageHeader.setCustomerServiceNumber(configuration().getString("checkout.customerServiceNumber"));
        return new SunrisePageData(pageHeader, new PageFooter(), content, getPageMeta(ctx, userContext));
    }

    private PageMeta getPageMeta(final Http.Context ctx, final UserContext userContext) {
        final PageMeta pageMeta = new PageMeta();
        pageMeta.setAssetsPath(reverseRouter().designAssets("").url());
        pageMeta.setBagQuantityOptions(IntStream.rangeClosed(1, 9).boxed().collect(toList()));
        pageMeta.setCsrfToken(SunriseController.getCsrfToken(ctx.session()));
        final String language = userContext.locale().getLanguage();
        pageMeta.addHalLink(reverseRouter().home(language), "home", "continueShopping")
                .addHalLink(reverseRouter().search(language), "search")
                .addHalLink(reverseRouter().changeLanguage(), "selectLanguage")
                .addHalLink(reverseRouter().changeCountry(language), "selectCountry")

                .addHalLink(reverseRouter().showCart(language), "cart")
                .addHalLink(reverseRouter().productToCartForm(language), "addToCart")
                .addHalLink(reverseRouter().processChangeLineItemQuantity(language), "changeLineItem")
                .addHalLink(reverseRouter().processDeleteLineItem(language), "deleteLineItem")

                .addHalLink(reverseRouter().showCheckoutAddressesForm(language), "checkout", "editShippingAddress", "editBillingAddress")
                .addHalLink(reverseRouter().processCheckoutAddressesForm(language), "checkoutAddressSubmit")
                .addHalLink(reverseRouter().showCheckoutShippingForm(language), "editShippingMethod")
                .addHalLink(reverseRouter().processCheckoutShippingForm(language), "checkoutShippingSubmit")
                .addHalLink(reverseRouter().showCheckoutPaymentForm(language), "editPaymentInfo")
                .addHalLink(reverseRouter().processCheckoutPaymentForm(language), "checkoutPaymentSubmit")
                .addHalLink(reverseRouter().processCheckoutConfirmationForm(language), "checkoutConfirmationSubmit")

                .addHalLinkOfHrefAndRel(ctx.request().uri(), "self");
        return pageMeta;
    }

    protected PriceFormatter priceFormatter(final UserContext userContext) {
        return PriceFormatter.of(userContext.locale());
    }

    protected UserContext userContext(final String languageTag) {
        final ProjectContext projectContext = projectContext();
        final List<Locale> acceptedLocales = acceptedLocales(languageTag, request(), projectContext);
        final CountryCode currentCountry = currentCountry(session(), projectContext);
        final CurrencyUnit currentCurrency = currentCurrency(currentCountry, projectContext);
        return UserContext.of(acceptedLocales, currentCountry, currentCurrency);
    }

    protected RequestContext requestContext() {
        return RequestContext.of(request().queryString(), request().path());
    }

    protected Optional<Category> newCategory() {
        return categoryNewExtId.flatMap(extId -> categoryTree().findByExternalId(extId));
    }

    private static Locale currentLocale(final String languageTag, final ProjectContext projectContext) {
        final Locale locale = Locale.forLanguageTag(languageTag);
        return projectContext.isLocaleAccepted(locale) ? locale : projectContext.defaultLocale();
    }

    private static CountryCode currentCountry(final Http.Session session, final ProjectContext projectContext) {
        final String countryCodeInSession = session.get(SESSION_COUNTRY);
        final CountryCode country = CountryCode.getByCode(countryCodeInSession, false);
        return projectContext.isCountryAccepted(country) ? country : projectContext.defaultCountry();
    }

    private static CurrencyUnit currentCurrency(final CountryCode currentCountry, final ProjectContext projectContext) {
        return Optional.ofNullable(currentCountry.getCurrency())
                .map(countryCurrency -> {
                    final CurrencyUnit currency = Monetary.getCurrency(countryCurrency.getCurrencyCode());
                    return projectContext.isCurrencyAccepted(currency) ? currency : projectContext.defaultCurrency();
                }).orElse(projectContext.defaultCurrency());
    }

    private static List<Locale> acceptedLocales(final String languageTag, final Http.Request request,
                                                final ProjectContext projectContext) {
        final ArrayList<Locale> acceptedLocales = new ArrayList<>();
        acceptedLocales.add(currentLocale(languageTag, projectContext));
        acceptedLocales.addAll(request.acceptLanguages().stream()
                .map(lang -> Locale.forLanguageTag(lang.code()))
                .collect(toList()));
        acceptedLocales.addAll(projectContext.locales());
        return acceptedLocales;
    }

    @Nullable
    private static String getCsrfToken(final Http.Session session) {
        return session.get("csrfToken");
    }
}