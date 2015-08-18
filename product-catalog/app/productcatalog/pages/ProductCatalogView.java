package productcatalog.pages;

import common.cms.CmsPage;
import common.contexts.AppContext;
import common.pages.PageContent;
import common.pages.PageData;
import common.pages.SunrisePageData;
import common.templates.TemplateService;
import play.twirl.api.Html;

public final class ProductCatalogView {
    private final TemplateService templateService;
    private final AppContext context;
    private final CmsPage commonCms;

    public ProductCatalogView(final TemplateService templateService, final AppContext context, final CmsPage commonCms) {
        this.templateService = templateService;
        this.context = context;
        this.commonCms = commonCms;
    }

    public Html productOverviewPage(final ProductOverviewPageContent content) {
        return templateService.renderToHtml("pop", pageData(content));
    }

    public Html productDetailPage(final ProductDetailPageContent content) {
        return templateService.renderToHtml("pdp", pageData(content));
    }

    private PageData pageData(final PageContent pageContent) {
        return SunrisePageData.of(commonCms, context,  pageContent);
    }
}
