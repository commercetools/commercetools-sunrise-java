package common.controllers;

import com.neovisionaries.i18n.CountryCode;
import common.cms.CmsPage;
import common.cms.CmsService;
import common.contexts.AppContext;
import common.contexts.UserContext;
import common.templates.TemplateService;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.play.controllers.ShopController;
import io.sphere.sdk.play.metrics.MetricAction;
import play.libs.F;
import play.mvc.Result;
import play.mvc.With;

import java.util.Collections;
import java.util.Locale;
import java.util.function.Function;

/**
 * An application specific controller.
 * Since we want to show a standard web shop it contains categories.
 */
@With(MetricAction.class)
public abstract class SunriseController extends ShopController {
    private final AppContext context;
    private final ControllerDependency controllerDependency;

    protected SunriseController(final ControllerDependency controllerDependency) {
        super(controllerDependency.client());
        // TODO Fill it properly
        final UserContext userContext = UserContext.of(Locale.GERMAN, Collections.<Locale>emptyList(), CountryCode.DE);
        this.context = AppContext.of(userContext, controllerDependency.projectContext());
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

    protected final AppContext context() {
        return context;
    }

    protected F.Promise<Result> withCommonCms(final Function<CmsPage, Result> pageRenderer) {
        final F.Promise<CmsPage> commonPage = cmsService().getPage(context().user().language(), "common");
        return commonPage.map(pageRenderer::apply);
    }

    protected F.Promise<Result> withCms(final String pageKey, final Function<CmsPage, F.Promise<Result>> action) {
        return cmsService().getPage(context().user().language(), pageKey).flatMap(action::apply);
    }
}
