package productcatalog.templates;

import common.templates.HandlebarsViewService;
import play.twirl.api.Html;
import productcatalog.models.ProductOverviewPageData;

public final class ProductCatalogViewService extends HandlebarsViewService {

    public Html productOverviewPage(final ProductOverviewPageData pageData) {
        return html("pop", pageData);
    }
}
