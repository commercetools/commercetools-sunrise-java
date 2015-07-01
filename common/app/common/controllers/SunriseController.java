package common.controllers;

import com.neovisionaries.i18n.CountryCode;
import common.cms.CmsPage;
import common.cms.CmsService;
import common.contexts.ProjectContext;
import common.contexts.UserContext;
import common.templates.TemplateService;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.play.controllers.ShopController;
import io.sphere.sdk.play.metrics.MetricAction;
import play.libs.F;
import play.mvc.Result;
import play.mvc.With;

import java.util.Locale;
import java.util.function.Function;

/**
 * An application specific controller.
 * Since we want to show a standard web shop it contains categories.
 */
@With(MetricAction.class)
public abstract class SunriseController extends ShopController {
    private final ControllerDependencies controllerDependencies;

    protected SunriseController(final PlayJavaSphereClient client, final ControllerDependencies controllerDependencies) {
        super(client);
        this.controllerDependencies = controllerDependencies;
    }

    protected final CategoryTree categories() {
        return controllerDependencies.categoryTree();
    }

    public TemplateService templateService() {
        return controllerDependencies.templateService();
    }

    protected final CmsService cmsService() {
        return controllerDependencies.cmsService();
    }

    protected final UserContext userContext() {
        return UserContext.of(Locale.GERMAN, CountryCode.DE);
    }

    protected final ProjectContext projectContext() {
        return controllerDependencies.projectContext();
    }

    protected F.Promise<Result> withCommonCms(final Function<CmsPage, Result> pageRenderer) {
        final F.Promise<CmsPage> commonPage = cmsService().getPage(userContext().language(), "common");
        return commonPage.map(pageRenderer::apply);
    }

    protected F.Promise<Result> withCms(final String pageKey, final Function<CmsPage, F.Promise<Result>> action) {
        return cmsService().getPage(userContext().language(), pageKey).flatMap(action::apply);
    }
}
