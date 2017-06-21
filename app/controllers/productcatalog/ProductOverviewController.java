package controllers.productcatalog;

import com.commercetools.sunrise.framework.components.controllers.PageHeaderControllerComponentSupplier;
import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.productcatalog.productoverview.CategoryFinder;
import com.commercetools.sunrise.productcatalog.productoverview.ProductListFinder;
import com.commercetools.sunrise.productcatalog.productoverview.SunriseProductOverviewController;
import com.commercetools.sunrise.productcatalog.productoverview.search.ProductOverviewSearchControllerComponentsSupplier;
import com.commercetools.sunrise.productcatalog.productoverview.viewmodels.ProductOverviewPageContentFactory;
import com.commercetools.sunrise.wishlist.MiniWishlistControllerComponent;
import play.mvc.Result;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@LogMetrics
@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        PageHeaderControllerComponentSupplier.class,
        ProductOverviewSearchControllerComponentsSupplier.class,
        MiniWishlistControllerComponent.class
})
public final class ProductOverviewController extends SunriseProductOverviewController {

    @Inject
    public ProductOverviewController(final ContentRenderer contentRenderer,
                                     final CategoryFinder categoryFinder,
                                     final ProductListFinder productListFinder,
                                     final ProductOverviewPageContentFactory pageContentFactory) {
        super(contentRenderer, categoryFinder, productListFinder, pageContentFactory);
    }

    @Nullable
    @Override
    public String getTemplateName() {
        return "pop";
    }

    @Override
    public CompletionStage<Result> handleNotFoundCategory() {
        return completedFuture(notFound());
    }
}