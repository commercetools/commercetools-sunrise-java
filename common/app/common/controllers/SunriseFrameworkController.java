package common.controllers;

import com.neovisionaries.i18n.CountryCode;
import common.contexts.ProjectContext;
import common.contexts.RequestContext;
import common.contexts.UserContext;
import common.models.LocationSelector;
import common.models.NavMenuDataFactory;
import common.template.cms.CmsService;
import common.template.engine.TemplateEngine;
import common.template.i18n.I18nResolver;
import common.utils.PriceFormatter;
import framework.ControllerComponent;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.utils.FutureUtils;
import play.Configuration;
import play.mvc.Controller;
import play.mvc.Http;
import shoppingcart.CartSessionUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

import static common.controllers.SunriseController.SESSION_COUNTRY;
import static java.util.stream.Collectors.toList;

public abstract class SunriseFrameworkController extends Controller {
    @Inject
    private SphereClient sphere;
    @Inject
    private UserContext userContext;
    @Inject
    private CategoryTree categoryTree;
    @Inject
    private ProjectContext projectContext;
    @Inject
    private TemplateEngine templateEngine;
    @Inject
    private CmsService cmsService;
    @Inject
    private Configuration configuration;
    @Inject
    private I18nResolver i18nResolver;
    private Optional<String> categoryNewExtId;
    private boolean showInfoModal;
    @Inject
    private PageMetaFactory pageMetaFactory;
    @Inject
    private NavMenuDataFactory navMenuDataFactory;
    private final List<ControllerComponent> controllerComponents = new LinkedList<>();

    protected SunriseFrameworkController() {
    }

    @Inject
    public void initializeFields(Configuration configuration) {
        System.err.println("init in SunriseFrameworkController");
        this.categoryNewExtId = Optional.ofNullable(configuration.getString("common.newCategoryExternalId"));
        this.showInfoModal = configuration.getBoolean("application.demoInfo.enabled", false);
    }

    public SphereClient sphere() {
        return sphere;
    }

    public CategoryTree categoryTree() {
        return categoryTree;
    }

    public ProjectContext projectContext() {
        return projectContext;
    }

    public TemplateEngine templateEngine() {
        return templateEngine;
    }

    public CmsService cmsService() {
        return cmsService;
    }

    public Configuration configuration() {
        return configuration;
    }

    public I18nResolver i18nResolver() {
        return i18nResolver;
    }

    protected final SunrisePageData pageData(final UserContext userContext, final PageContent content,
                                             final Http.Context ctx, final Http.Session session) {
        final PageHeader pageHeader = new PageHeader(content.getAdditionalTitle());
        pageHeader.setLocation(new LocationSelector(projectContext(), userContext));
        pageHeader.setNavMenu(navMenuDataFactory.create());
        pageHeader.setMiniCart(CartSessionUtils.getMiniCart(session));
        pageHeader.setCustomerServiceNumber(configuration().getString("checkout.customerServiceNumber"));
        return new SunrisePageData(pageHeader, new PageFooter(), content, pageMetaFactory.create());
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
    public static String getCsrfToken(final Http.Session session) {
        return session.get("csrfToken");
    }

    public UserContext userContext() {
        return userContext;
    }

    protected final void registerControllerComponent(final ControllerComponent controllerComponent) {
        controllerComponents.add(controllerComponent);
    }


    protected final <T> CompletionStage<Object> runAsyncHook(final Class<T> hookClass, final Function<T, CompletionStage<?>> f) {
        //TODO throw a helpful NPE if component returns null instead of CompletionStage
        final List<CompletionStage<Void>> collect = controllerComponents.stream()
                .filter(x -> hookClass.isAssignableFrom(x.getClass()))
                .map(hook -> f.apply((T) hook))
                .map(stage -> (CompletionStage<Void>) stage)
                .collect(Collectors.toList());
        final CompletionStage<?> listCompletionStage = FutureUtils.listOfFuturesToFutureOfList(collect);
        return listCompletionStage.thenApply(z -> null);
    }
}
