package common.controllers;

import common.cms.CmsPage;
import common.cms.CmsService;
import common.contexts.ProjectContext;
import common.countries.CountryOperations;
import common.contexts.UserContext;
import common.templates.TemplateService;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.play.controllers.ShopController;
import io.sphere.sdk.play.metrics.MetricAction;
import play.Configuration;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

import java.util.function.Function;

/**
 * An application specific controller.
 * Since we want to show a standard web shop it contains categories.
 */
@With(MetricAction.class)
public abstract class SunriseController extends ShopController {
    private final CategoryTree categoryTree;
    private final CountryOperations countryOperations;
    private final ProjectContext projectContext;
    private final TemplateService templateService;
    private final CmsService cmsService;

    protected SunriseController(final PlayJavaSphereClient client, final CategoryTree categoryTree, final ProjectContext projectContext,
                                final Configuration configuration, final TemplateService templateService, final CmsService cmsService) {
        super(client);
        this.categoryTree = categoryTree;
        this.countryOperations = CountryOperations.of(configuration);
        this.projectContext = projectContext;
        this.templateService = templateService;
        this.cmsService = cmsService;
    }

    protected final CategoryTree categories() {
        return categoryTree;
    }

    public TemplateService templateService() {
        return templateService;
    }

    protected final CmsService cmsService() {
        return cmsService;
    }

    protected final UserContext userContext() {
        return UserContext.of(Controller.lang(), countryOperations.country());
    }

    protected final ProjectContext projectContext() {
        return projectContext;
    }

    protected F.Promise<Result> withCommonCms(final Function<CmsPage, Result> pageRenderer) {
        final F.Promise<CmsPage> commonPage = cmsService().getPage(userContext().locale(), "common");
        return commonPage.map(pageRenderer::apply);
    }

    protected F.Promise<Result> withCms(final String pageKey, final Function<CmsPage, F.Promise<Result>> action) {
        return cmsService().getPage(userContext().locale(), pageKey).flatMap(action::apply);
    }
}
