package controllers.productcatalog;

import com.commercetools.sunrise.core.components.RegisteredComponents;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.models.products.ProductFetcher;
import com.commercetools.sunrise.productcatalog.productdetail.ProductProjectionRecommendationsControllerComponent;
import com.commercetools.sunrise.models.products.ProductVariantFinder;
import com.commercetools.sunrise.productcatalog.productdetail.SunriseProductDetailController;
import com.commercetools.sunrise.productcatalog.productdetail.viewmodels.ProductDetailPageContentFactory;
import io.sphere.sdk.products.ProductProjection;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@LogMetrics
@NoCache
@RegisteredComponents(ProductProjectionRecommendationsControllerComponent.class)
public final class ProductDetailController extends SunriseProductDetailController {

    private final ProductReverseRouter productReverseRouter;

    @Inject
    public ProductDetailController(final ContentRenderer contentRenderer,
                                   final ProductFetcher productFinder,
                                   final ProductVariantFinder productVariantFinder,
                                   final ProductDetailPageContentFactory pageContentFactory,
                                   final ProductReverseRouter productReverseRouter) {
        super(contentRenderer, productFinder, productVariantFinder, pageContentFactory);
        this.productReverseRouter = productReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "pdp";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }

    @Override
    public CompletionStage<Result> handleNotFoundProduct() {
        return completedFuture(notFound());
    }

    @Override
    public CompletionStage<Result> handleNotFoundProductVariant(final ProductProjection product) {
        return productReverseRouter
                .productDetailPageCall(product, product.getMasterVariant())
                .map(this::redirectToCall)
                .orElseGet(() -> completedFuture(notFound()));
    }
}