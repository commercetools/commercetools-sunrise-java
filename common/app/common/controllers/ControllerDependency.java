package common.controllers;

import common.cms.CmsService;
import common.contexts.ProjectContext;
import common.pages.ReverseRouter;
import common.templates.TemplateService;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.models.Base;
import play.Configuration;
import play.i18n.MessagesApi;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ControllerDependency extends Base {
    private final PlayJavaSphereClient sphere;
    private final CategoryTree categoryTree;
    private final ProjectContext projectContext;
    private final TemplateService templateService;
    private final CmsService cmsService;
    private final Configuration configuration;
    private final MessagesApi messagesApi;
    private final ReverseRouter reverseRouter;

    @Inject
    public ControllerDependency(final PlayJavaSphereClient sphere, final CategoryTree categoryTree, final ProjectContext projectContext,
                                final TemplateService templateService, final CmsService cmsService, final Configuration configuration,
                                final MessagesApi messagesApi, final ReverseRouter reverseRouter) {
        this.sphere = sphere;
        this.categoryTree = categoryTree;
        this.projectContext = projectContext;
        this.templateService = templateService;
        this.cmsService = cmsService;
        this.configuration = configuration;
        this.messagesApi = messagesApi;
        this.reverseRouter = reverseRouter;
    }

    public PlayJavaSphereClient sphere() {
        return sphere;
    }

    public CategoryTree categoryTree() {
        return categoryTree;
    }

    public ProjectContext projectContext() {
        return projectContext;
    }

    public TemplateService templateService() {
        return templateService;
    }

    public CmsService cmsService() {
        return cmsService;
    }

    public Configuration configuration() {
        return configuration;
    }

    public MessagesApi messagesApi() {
        return messagesApi;
    }

    public ReverseRouter getReverseRouter() {
        return reverseRouter;
    }
}
