package common.controllers;

import common.cms.CmsService;
import common.contexts.ProjectContext;
import common.templates.TemplateService;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaSphereClient;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ControllerDependency {
    private final PlayJavaSphereClient sphere;
    private final CategoryTree categoryTree;
    private final ProjectContext projectContext;
    private final TemplateService templateService;
    private final CmsService cmsService;
    private Configuration configuration;

    @Inject
    public ControllerDependency(final PlayJavaSphereClient sphere, final CategoryTree categoryTree, final ProjectContext projectContext, final TemplateService templateService, final CmsService cmsService, final Configuration configuration) {
        this.sphere = sphere;
        this.categoryTree = categoryTree;
        this.projectContext = projectContext;
        this.templateService = templateService;
        this.cmsService = cmsService;
        this.configuration = configuration;
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
}
