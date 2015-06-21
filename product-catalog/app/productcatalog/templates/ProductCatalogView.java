package productcatalog.templates;

import common.cms.CmsPage;
import common.pages.PageContent;
import common.pages.PageData;
import common.pages.SunrisePageData;
import common.templates.ViewService;
import play.twirl.api.Html;
import productcatalog.models.ProductOverviewPageContent;

public final class ProductCatalogView {
    private final ViewService viewService;
    private final CmsPage cms;

    public ProductCatalogView(final ViewService viewService, final CmsPage cms) {
        this.viewService = viewService;
        this.cms = cms;
    }

    public Html productOverviewPage(final ProductOverviewPageContent content) {
        return viewService.fillToHtml("pop", pageData(content));
    }

    private PageData pageData(final PageContent pageContent) {
        return SunrisePageData.of(cms, pageContent);
    }
}
