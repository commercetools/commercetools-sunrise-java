package productcatalog.templates;

import common.templates.ViewService;
import play.twirl.api.Html;
import productcatalog.models.ProductOverviewPageData;

public final class ProductCatalogViewService {
    private final ViewService viewService;

    public ProductCatalogViewService(final ViewService viewService) {
        this.viewService = viewService;
    }

    public Html productOverviewPage(final ProductOverviewPageData pageData) {
        return viewService.apply("pop", pageData);
    }
}
