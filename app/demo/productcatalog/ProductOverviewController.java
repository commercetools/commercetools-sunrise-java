package demo.productcatalog;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.search.SearchControllerComponentListSupplier;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import com.commercetools.sunrise.productcatalog.productoverview.CategoryFinder;
import com.commercetools.sunrise.productcatalog.productoverview.ProductListFinder;
import com.commercetools.sunrise.productcatalog.productoverview.SunriseProductOverviewController;
import com.commercetools.sunrise.productcatalog.productoverview.view.ProductOverviewPageContentFactory;
import demo.CommonControllerComponentListSupplier;
import demo.PageHeaderControllerComponentListSupplier;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@NoCache
public final class ProductOverviewController extends SunriseProductOverviewController {

    @Inject
    public ProductOverviewController(final ComponentRegistry componentRegistry,
                                     final TemplateRenderer templateRenderer,
                                     final CategoryFinder categoryFinder,
                                     final ProductListFinder productListFinder,
                                     final ProductOverviewPageContentFactory productOverviewPageContentFactory) {
        super(componentRegistry, templateRenderer, categoryFinder, productListFinder, productOverviewPageContentFactory);
    }

    @Inject
    public void registerComponents(final CommonControllerComponentListSupplier commonControllerComponentListSupplier,
                                   final PageHeaderControllerComponentListSupplier pageHeaderControllerComponentListSupplier,
                                   final SearchControllerComponentListSupplier searchControllerComponentListSupplier) {
        register(commonControllerComponentListSupplier);
        register(pageHeaderControllerComponentListSupplier);
        register(searchControllerComponentListSupplier);
    }

    @Override
    public CompletionStage<Result> handleNotFoundCategory() {
        return completedFuture(notFound());
    }
}