package common.controllers;

import common.cms.CmsService;
import common.contexts.ProjectContext;
import common.contexts.RequestContext;
import common.contexts.UserContext;
import common.models.LocationSelector;
import common.models.NavMenuData;
import common.templates.TemplateService;
import common.utils.PriceFormatter;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.play.controllers.ShopController;
import io.sphere.sdk.play.metrics.MetricAction;
import play.Configuration;
import play.filters.csrf.AddCSRFToken;
import play.i18n.Lang;
import play.i18n.Messages;
import play.mvc.With;

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
    private final ControllerDependency controllerDependency;
    private final String saleCategoryExtId;

    protected SunriseController(final ControllerDependency controllerDependency) {
        super(controllerDependency.sphere());
        this.saleCategoryExtId = controllerDependency.configuration().getString("common.saleCategoryExternalId", "");

        // TODO Fill it properly
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

    protected ProjectContext projectContext() {
        return controllerDependency.projectContext();
    }

    protected final SunrisePageData pageData(final UserContext userContext, final PageContent content) {
        final PageHeader pageHeader = new PageHeader(content.getAdditionalTitle());
        pageHeader.setLocation(new LocationSelector(projectContext(), userContext));
        pageHeader.setNavMenu(new NavMenuData(categories(), userContext, reverseRouter(), saleCategoryExtId));
        return new SunrisePageData(pageHeader, new PageFooter(), content, new PageMeta());
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

    protected String getCsrfToken() {
        return session("csrfToken");
    }
}
