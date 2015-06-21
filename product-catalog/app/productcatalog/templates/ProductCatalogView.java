package productcatalog.templates;

import common.cms.CmsPage;
import common.pages.PageContent;
import common.pages.PageData;
import common.pages.SunrisePageData;
import common.templates.TemplateService;
import play.twirl.api.Html;
import productcatalog.models.ProductOverviewPageContent;

public final class ProductCatalogView {
    private final TemplateService templateService;
    private final CmsPage commonCms;

    public ProductCatalogView(final TemplateService templateService, final CmsPage commonCms) {
        this.templateService = templateService;
        this.commonCms = commonCms;
    }

    public Html productOverviewPage(final ProductOverviewPageContent content) {
        return templateService.fillToHtml("pop", pageData(content));
    }

    private PageData pageData(final PageContent pageContent) {
        return SunrisePageData.of(commonCms, pageContent);
    }
}
