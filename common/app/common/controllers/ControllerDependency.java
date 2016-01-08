package common.controllers;

import common.cms.CmsService;
import common.contexts.ProjectContext;
import common.i18n.I18nResolver;
import common.templates.TemplateService;
import io.sphere.sdk.categories.CategoryTreeExtended;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.models.Base;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ControllerDependency extends Base {
    private final PlayJavaSphereClient sphere;
    private final CategoryTreeExtended categoryTree;
    private final ProjectContext projectContext;
    private final TemplateService templateService;
    private final CmsService cmsService;
    private final Configuration configuration;
    private final I18nResolver i18nResolver;
    private final ReverseRouter reverseRouter;

    @Inject
    public ControllerDependency(final PlayJavaSphereClient sphere, final CategoryTreeExtended categoryTree, final ProjectContext projectContext,
                                final TemplateService templateService, final CmsService cmsService, final Configuration configuration,
                                final I18nResolver i18nResolver, final ReverseRouter reverseRouter) {
        this.sphere = sphere;
        this.categoryTree = categoryTree;
        this.projectContext = projectContext;
        this.templateService = templateService;
        this.cmsService = cmsService;
        this.configuration = configuration;
        this.i18nResolver = i18nResolver;
        this.reverseRouter = reverseRouter;
    }

    public PlayJavaSphereClient sphere() {
        return sphere;
    }

    public CategoryTreeExtended categoryTree() {
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

    public I18nResolver i18nResolver() {
        return i18nResolver;
    }

    public ReverseRouter getReverseRouter() {
        return reverseRouter;
    }
}
