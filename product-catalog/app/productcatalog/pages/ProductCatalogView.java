package productcatalog.pages;

import common.cms.CmsPage;
import common.contexts.ProjectContext;
import common.pages.PageContent;
import common.pages.PageData;
import common.pages.SunrisePageData;
import common.templates.TemplateService;
import play.twirl.api.Html;

public final class ProductCatalogView {
    private final TemplateService templateService;
    private final ProjectContext projectContext;
    private final CmsPage commonCms;

    public ProductCatalogView(final TemplateService templateService, final ProjectContext projectContext, final CmsPage commonCms) {
        this.templateService = templateService;
        this.projectContext = projectContext;
        this.commonCms = commonCms;
    }

    public Html productOverviewPage(final ProductOverviewPageContent content) {
        return templateService.renderToHtml("pop", pageData(content));
    }

    private PageData pageData(final PageContent pageContent) {
        return SunrisePageData.of(commonCms, projectContext,  pageContent);
    }
}
