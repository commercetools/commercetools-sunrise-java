package demo.productcatalog;

import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.productcatalog.productdetail.ProductFinder;
import com.commercetools.sunrise.productcatalog.productdetail.ProductVariantFinder;
import com.commercetools.sunrise.productcatalog.productdetail.SunriseProductDetailController;
import com.commercetools.sunrise.productcatalog.productdetail.view.ProductDetailPageContentFactory;
import com.commercetools.sunrise.productcatalog.productsuggestions.ProductSuggestionsControllerComponent;
import io.sphere.sdk.products.ProductProjection;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class ProductDetailController extends SunriseProductDetailController {

    private final ProductReverseRouter productReverseRouter;

    @Inject
    public ProductDetailController(final TemplateRenderer templateRenderer,
                                   final RequestHookContext hookContext,
                                   final ProductFinder productFinder,
                                   final ProductVariantFinder productVariantFinder,
                                   final ProductDetailPageContentFactory productDetailPageContentFactory,
                                   final ProductReverseRouter productReverseRouter) {
        super(templateRenderer, hookContext, productFinder, productVariantFinder, productDetailPageContentFactory);
        this.productReverseRouter = productReverseRouter;
    }

    @Inject
    public void setSuggestionsComponent(final ProductSuggestionsControllerComponent component) {
        registerControllerComponent(component);
    }

    @Override
    protected CompletionStage<Result> handleNotFoundProduct() {
        return completedFuture(notFound());
    }

    @Override
    protected CompletionStage<Result> handleNotFoundVariant(final ProductProjection product) {
        return productReverseRouter.productDetailPageCall(product, product.getMasterVariant())
                .map(this::redirectTo)
                .orElseGet(() -> completedFuture(notFound()));
    }
}