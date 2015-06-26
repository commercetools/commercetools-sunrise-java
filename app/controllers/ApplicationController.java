package controllers;

import common.cms.CmsService;
import common.contexts.ProjectContext;
import common.controllers.SunriseController;
import common.templates.TemplateService;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaSphereClient;
import play.Configuration;
import play.libs.F;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Controller for main web pages like index, imprint and contact.
 */
@Singleton
public final class ApplicationController extends SunriseController {

    @Inject
    public ApplicationController(final PlayJavaSphereClient client, final CategoryTree categoryTree, final ProjectContext projectContext,
                                 final Configuration configuration, final TemplateService templateService, final CmsService cmsService) {
        super(client, categoryTree, projectContext, configuration, templateService, cmsService);
    }

    public F.Promise<Result> index() {
        return F.Promise.pure(redirect(productcatalog.controllers.routes.ProductCatalogController.pop()));
    }
}
