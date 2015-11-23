package common.controllers;

import common.cms.CmsService;
import common.contexts.AppContext;
import common.contexts.UserContext;
import common.pages.PageContent;
import common.pages.ReverseRouter;
import common.pages.SunrisePageData;
import common.pages.SunrisePageDataFactory;
import common.templates.TemplateService;
import common.utils.PriceFormatter;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.play.controllers.ShopController;
import io.sphere.sdk.play.metrics.MetricAction;
import play.Configuration;
import play.filters.csrf.AddCSRFToken;
import play.i18n.Lang;
import play.i18n.Messages;
import play.mvc.Http;
import play.mvc.With;

import javax.annotation.Nullable;
import javax.money.Monetary;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Locale;

import static com.neovisionaries.i18n.CountryCode.DE;
import static java.util.stream.Collectors.toList;

/**
 * An application specific controller.
 * Since we want to show a standard web shop it contains categories.
 */
@With(MetricAction.class)
@AddCSRFToken
public abstract class SunriseController extends ShopController {
    private final AppContext context;
    private final ControllerDependency controllerDependency;

    protected SunriseController(final ControllerDependency controllerDependency) {
        super(controllerDependency.sphere());
        // TODO Fill it properly
        this.context = AppContext.of(controllerDependency.projectContext());
        this.controllerDependency = controllerDependency;
    }

    protected final CategoryTree categories() {
        return controllerDependency.categoryTree();
    }

    public TemplateService templateService() {
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

    protected final AppContext context() {
        return context;
    }

    protected final SunrisePageData pageData(final UserContext userContext, final PageContent content, final Http.Context ctx) {
        final Messages messages = messages(userContext);
        final String saleCategoryExtId = configuration().getString("common.saleCategoryExternalId");
        return new SunrisePageDataFactory(messages, userContext, context().project(), categories(),
                controllerDependency.getReverseRouter(), saleCategoryExtId, ctx).create(content);
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

    protected String getCsrfToken() {
        final Http.Session session = session();
        return getCsrfToken(session);
    }

    @Nullable
    public static String getCsrfToken(final Http.Session session) {
        return session.get("csrfToken");
    }
}
