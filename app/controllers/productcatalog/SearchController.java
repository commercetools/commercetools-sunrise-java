package controllers.productcatalog;

import com.commercetools.sunrise.core.components.controllers.PageHeaderControllerComponentSupplier;
import com.commercetools.sunrise.core.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.productcatalog.productoverview.ProductListFinder;
import com.commercetools.sunrise.productcatalog.productoverview.SunriseSearchController;
import com.commercetools.sunrise.productcatalog.productoverview.search.ProductOverviewSearchControllerComponentsSupplier;
import com.commercetools.sunrise.productcatalog.productoverview.viewmodels.ProductOverviewPageContentFactory;

import javax.inject.Inject;

@LogMetrics
@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        PageHeaderControllerComponentSupplier.class,
        ProductOverviewSearchControllerComponentsSupplier.class
})
public final class SearchController extends SunriseSearchController {

    @Inject
    public SearchController(final ContentRenderer contentRenderer,
                            final ProductListFinder productListFinder,
                            final ProductOverviewPageContentFactory pageContentFactory) {
        super(contentRenderer, productListFinder, pageContentFactory);
    }

    @Override
    public String getTemplateName() {
        return "pop";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }
}