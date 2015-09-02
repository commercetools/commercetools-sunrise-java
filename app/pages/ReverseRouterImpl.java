package pages;

import common.pages.ReverseRouter;
import io.sphere.sdk.models.Base;
import play.mvc.Call;
import productcatalog.controllers.routes;

public class ReverseRouterImpl extends Base implements ReverseRouter {

    @Override
    public Call category(final String language, final String slug, final int page) {
        return routes.ProductOverviewPageController.show(language, slug, page);
    }
}
