package pages;

import common.pages.ReverseRouter;
import io.sphere.sdk.models.Base;
import play.Configuration;
import play.mvc.Call;
import productcatalog.controllers.routes;

public class ReverseRouterImpl extends Base implements ReverseRouter {
    private final int pageSizeDefault;

    public ReverseRouterImpl(final Configuration configuration) {
        this.pageSizeDefault = configuration.getInt("pop.pageSize.default");
    }

    @Override
    public Call home(final String languageTag) {
        //return controllers.routes.HomeController.show(languageTag);
        return null;
    }

    @Override
    public Call category(final String languageTag, final String categorySlug, final int page) {
        return routes.ProductOverviewPageController.show(languageTag, page, pageSizeDefault, categorySlug);
    }

    @Override
    public Call category(final String languageTag, final String categorySlug) {
        return category(languageTag, categorySlug, 1);
    }

    @Override
    public Call search(final String languageTag, final String searchTerm, final int page) {
        return productcatalog.controllers.routes.ProductOverviewPageController.search(languageTag, page, pageSizeDefault, searchTerm);
    }

    @Override
    public Call search(final String languageTag, final String searchTerm) {
        return search(languageTag, searchTerm, 1);
    }

    @Override
    public Call product(final String languageTag, final String productSlug, final String sku) {
        return productcatalog.controllers.routes.ProductDetailPageController.show(languageTag, productSlug, sku);
    }

    @Override
    public Call productVariantToCartForm(final String languageTag) {
        //return cart.routes.LineItemAddController.process(languageTag);
        return null;
    }
}
