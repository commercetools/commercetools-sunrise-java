package demo.productcatalog;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import com.commercetools.sunrise.productcatalog.productdetail.ProductFinder;
import com.commercetools.sunrise.productcatalog.productdetail.ProductVariantFinder;
import com.commercetools.sunrise.productcatalog.productdetail.SunriseProductDetailController;
import com.commercetools.sunrise.productcatalog.productdetail.view.ProductDetailPageContentFactory;
import com.commercetools.sunrise.productcatalog.productsuggestions.ProductSuggestionsControllerComponent;
import demo.CommonControllerComponentListSupplier;
import demo.PageHeaderControllerComponentListSupplier;
import io.sphere.sdk.products.ProductProjection;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@NoCache
public final class ProductDetailController extends SunriseProductDetailController {

    private final ProductReverseRouter productReverseRouter;

    @Inject
    public ProductDetailController(final ComponentRegistry componentRegistry,
                                   final TemplateRenderer templateRenderer,
                                   final ProductFinder productFinder,
                                   final ProductVariantFinder productVariantFinder,
                                   final ProductDetailPageContentFactory productDetailPageContentFactory,
                                   final ProductReverseRouter productReverseRouter) {
        super(componentRegistry, templateRenderer, productFinder, productVariantFinder, productDetailPageContentFactory);
        this.productReverseRouter = productReverseRouter;
    }

    @Inject
    public void registerComponents(final CommonControllerComponentListSupplier commonControllerComponentListSupplier,
                                   final PageHeaderControllerComponentListSupplier pageHeaderControllerComponentListSupplier,
                                   final ProductSuggestionsControllerComponent productSuggestionsControllerComponent) {
        register(commonControllerComponentListSupplier);
        register(pageHeaderControllerComponentListSupplier);
        register(productSuggestionsControllerComponent);
    }

    @Override
    public CompletionStage<Result> handleNotFoundProduct() {
        return completedFuture(notFound());
    }

    @Override
    public CompletionStage<Result> handleNotFoundProductVariant(final ProductProjection product) {
        return productReverseRouter.productDetailPageCall(product, product.getMasterVariant())
                .map(this::redirectTo)
                .orElseGet(() -> completedFuture(notFound()));
    }
}