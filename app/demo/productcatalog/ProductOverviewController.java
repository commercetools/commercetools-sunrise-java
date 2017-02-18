package demo.productcatalog;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.search.SearchControllerComponentSupplier;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.RegisteredComponents;
import com.commercetools.sunrise.productcatalog.productoverview.CategoryFinder;
import com.commercetools.sunrise.productcatalog.productoverview.ProductListFinder;
import com.commercetools.sunrise.productcatalog.productoverview.SunriseProductOverviewController;
import com.commercetools.sunrise.productcatalog.productoverview.view.ProductOverviewPageContentFactory;
import com.commercetools.sunrise.common.CommonControllerComponentSupplier;
import demo.PageHeaderControllerComponentSupplier;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@NoCache
@RegisteredComponents({
        SearchControllerComponentSupplier.class,
        CommonControllerComponentSupplier.class,
        PageHeaderControllerComponentSupplier.class
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