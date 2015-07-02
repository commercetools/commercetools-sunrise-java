package common.controllers;

import common.cms.CmsService;
import common.contexts.ProjectContext;
import common.templates.TemplateService;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaSphereClient;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ControllerDependency {
    private final PlayJavaSphereClient client;
    private final CategoryTree categoryTree;
    private final ProjectContext projectContext;
    private final TemplateService templateService;
    private final CmsService cmsService;

    @Inject
    public ControllerDependency(final PlayJavaSphereClient client, final CategoryTree categoryTree,
                                final ProjectContext projectContext, final TemplateService templateService, final CmsService cmsService) {
        this.client = client;
        this.categoryTree = categoryTree;
        this.projectContext = projectContext;
        this.templateService = templateService;
        this.cmsService = cmsService;
    }

    public PlayJavaSphereClient client() {
        return client;
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
}
