package common.controllers;

import com.neovisionaries.i18n.CountryCode;
import common.actions.NoCache;
import common.contexts.ProjectContext;
import common.contexts.RequestContext;
import common.contexts.UserContext;
import common.models.LocationSelector;
import common.models.NavMenuData;
import common.template.cms.CmsService;
import common.template.engine.TemplateEngine;
import common.template.i18n.I18nResolver;
import common.utils.PriceFormatter;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.play.controllers.ShopController;
import io.sphere.sdk.play.metrics.MetricAction;
import myaccount.CustomerSessionUtils;
import play.Configuration;
import play.mvc.Http;
import play.mvc.With;
import shoppingcart.CartSessionUtils;

import javax.annotation.Nullable;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * An application specific controller.
 */
@With(MetricAction.class)
@NoCache
public abstract class SunriseController extends ShopController {

    public static final String SESSION_COUNTRY = "countryCode";
    private final ControllerDependency controllerDependency;
    private final Optional<String> saleCategoryExtId;
    private final Optional<String> categoryNewExtId;
    private final boolean showInfoModal;

    protected SunriseController(final ControllerDependency controllerDependency) {
        super(controllerDependency.sphere());
        this.controllerDependency = controllerDependency;
        final Configuration configuration = controllerDependency.configuration();
        this.saleCategoryExtId = Optional.ofNullable(configuration.getString("common.saleCategoryExternalId"));
        this.categoryNewExtId = Optional.ofNullable(configuration.getString("common.newCategoryExternalId"));
        this.showInfoModal = configuration.getBoolean("application.demoInfo.enabled", false);
    }

    protected final CategoryTree categoryTree() {
        return controllerDependency.categoryTree();
    }

    protected TemplateEngine templateEngine() {
        return controllerDependency.templateEngine();
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

    protected final SunrisePageData pageData(final UserContext userContext, final PageContent content,
                                             final Http.Context ctx, final Http.Session session) {
        final PageHeader pageHeader = new PageHeader(content.getAdditionalTitle());
        pageHeader.setLocation(new LocationSelector(projectContext(), userContext));
        pageHeader.setNavMenu(new NavMenuData(categoryTree(), userContext, reverseRouter(), saleCategoryExtId.orElse(null)));
        pageHeader.setMiniCart(CartSessionUtils.getMiniCart(session));
        pageHeader.setCustomerServiceNumber(configuration().getString("checkout.customerServiceNumber"));
        return new SunrisePageData(pageHeader, new PageFooter(), content, getPageMeta(userContext, ctx, session));
    }

    private PageMeta getPageMeta(final UserContext userContext, final Http.Context ctx, final Http.Session session) {
        final PageMeta pageMeta = new PageMeta();
        pageMeta.setUser(CustomerSessionUtils.getUserBean(session()));
        pageMeta.setAssetsPath(reverseRouter().themeAssets("").url());
        pageMeta.setBagQuantityOptions(IntStream.rangeClosed(1, 9).boxed().collect(toList()));
        pageMeta.setCsrfToken(SunriseController.getCsrfToken(ctx.session()));
        final String language = userContext.locale().getLanguage();
        pageMeta.addHalLink(reverseRouter().showHome(language), "home", "continueShopping")
                .addHalLink(reverseRouter().processSearchProductsForm(language), "search")
                .addHalLink(reverseRouter().processChangeLanguageForm(), "selectLanguage")
                .addHalLink(reverseRouter().processChangeCountryForm(language), "selectCountry")

                .addHalLink(reverseRouter().showCart(language), "cart")
                .addHalLink(reverseRouter().processAddProductToCartForm(language), "addToCart")
                .addHalLink(reverseRouter().processChangeLineItemQuantityForm(language), "changeLineItem")
                .addHalLink(reverseRouter().processDeleteLineItemForm(language), "deleteLineItem")

                .addHalLink(reverseRouter().showCheckoutAddressesForm(language), "checkout", "editShippingAddress", "editBillingAddress")
                .addHalLink(reverseRouter().processCheckoutAddressesForm(language), "checkoutAddressSubmit")
                .addHalLink(reverseRouter().showCheckoutShippingForm(language), "editShippingMethod")
                .addHalLink(reverseRouter().processCheckoutShippingForm(language), "checkoutShippingSubmit")
                .addHalLink(reverseRouter().showCheckoutPaymentForm(language), "editPaymentInfo")
                .addHalLink(reverseRouter().processCheckoutPaymentForm(language), "checkoutPaymentSubmit")
                .addHalLink(reverseRouter().processCheckoutConfirmationForm(language), "checkoutConfirmationSubmit")

                .addHalLink(reverseRouter().showLogInForm(language), "signIn", "logIn", "signUp")
                .addHalLink(reverseRouter().processLogInForm(language), "logInSubmit")
                .addHalLink(reverseRouter().processSignUpForm(language), "signUpSubmit")
                .addHalLink(reverseRouter().processLogOut(language), "logOut")

                .addHalLink(reverseRouter().processMyPersonalDetailsForm(language), "editMyPersonalDetails")

                .addHalLinkOfHrefAndRel(ctx.request().uri(), "self");
        newCategory().flatMap(nc -> reverseRouter().showCategory(userContext.locale(), nc))
                .ifPresent(call -> pageMeta.addHalLink(call, "newProducts"));
        pageMeta.setShowInfoModal(showInfoModal);
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

    protected RequestContext requestContext(final Http.Request request) {
        return RequestContext.of(request.queryString(), request.path());
    }

    protected Optional<Category> newCategory() {
        return categoryNewExtId.flatMap(extId -> categoryTree().findByExternalId(extId));
    }

    private static Locale currentLocale(final String languageTag, final ProjectContext projectContext) {
        final Locale locale = Locale.forLanguageTag(languageTag);
        return projectContext.isLocaleAccepted(locale) ? locale : projectContext.defaultLocale();
    }

    public static CountryCode currentCountry(final Http.Session session, final ProjectContext projectContext) {
        final String countryCodeInSession = session.get(SESSION_COUNTRY);
        final CountryCode country = CountryCode.getByCode(countryCodeInSession, false);
        return projectContext.isCountryAccepted(country) ? country : projectContext.defaultCountry();
    }

    public static CurrencyUnit currentCurrency(final CountryCode currentCountry, final ProjectContext projectContext) {
        return Optional.ofNullable(currentCountry.getCurrency())
                .map(countryCurrency -> {
                    final CurrencyUnit currency = Monetary.getCurrency(countryCurrency.getCurrencyCode());
                    return projectContext.isCurrencyAccepted(currency) ? currency : projectContext.defaultCurrency();
                }).orElseGet(projectContext::defaultCurrency);
    }

    public static List<Locale> acceptedLocales(final String languageTag, final Http.Request request,
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