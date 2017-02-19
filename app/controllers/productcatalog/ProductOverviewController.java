package controllers.productcatalog;

import com.commercetools.sunrise.common.ctp.metrics.LogMetrics;
import com.commercetools.sunrise.common.search.SearchControllerComponentsSupplier;
import com.commercetools.sunrise.framework.components.CommonControllerComponentsSupplier;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.productcatalog.productoverview.CategoryFinder;
import com.commercetools.sunrise.productcatalog.productoverview.ProductListFinder;
import com.commercetools.sunrise.productcatalog.productoverview.SunriseProductOverviewController;
import com.commercetools.sunrise.productcatalog.productoverview.view.ProductOverviewPageContentFactory;
import controllers.PageHeaderControllerComponentsSupplier;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@LogMetrics
@NoCache
@RegisteredComponents({
        SearchControllerComponentsSupplier.class,
        CommonControllerComponentsSupplier.class,
        PageHeaderControllerComponentsSupplier.class
})
public final class ProductOverviewController extends SunriseProductOverviewController {

    @Inject
    public ProductOverviewController(final TemplateRenderer templateRenderer,
                                     final CategoryFinder categoryFinder,
                                     final ProductListFinder productListFinder,
                                     final ProductOverviewPageContentFactory productOverviewPageContentFactory) {
        super(templateRenderer, categoryFinder, productListFinder, productOverviewPageContentFactory);
    }

    @Override
    public String getTemplateName() {
        return "pop";
    }

    @Override
    public CompletionStage<Result> handleNotFoundCategory() {
        return completedFuture(notFound());
    }
}