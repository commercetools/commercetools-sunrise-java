package controllers.productcatalog;

import com.commercetools.sunrise.common.search.SearchControllerComponentsSupplier;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.productcatalog.productoverview.ProductListFinder;
import com.commercetools.sunrise.productcatalog.productoverview.SunriseSearchController;
import com.commercetools.sunrise.productcatalog.productoverview.viewmodels.ProductOverviewPageContentFactory;
import controllers.PageHeaderControllerComponentSupplier;

import javax.inject.Inject;

@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        PageHeaderControllerComponentSupplier.class,
        SearchControllerComponentsSupplier.class
})
public final class SearchController extends SunriseSearchController {

    @Inject
    public SearchController(final TemplateRenderer templateRenderer,
                            final ProductListFinder productListFinder,
                            final ProductOverviewPageContentFactory productOverviewPageContentFactory) {
        super(templateRenderer, productListFinder, productOverviewPageContentFactory);
    }

    @Override
    public String getTemplateName() {
        return "pop";
    }
}