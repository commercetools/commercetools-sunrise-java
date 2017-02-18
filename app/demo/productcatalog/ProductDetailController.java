package demo.productcatalog;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.reverserouter.productcatalog.ProductReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.RegisteredComponents;
import com.commercetools.sunrise.productcatalog.productdetail.ProductFinder;
import com.commercetools.sunrise.productcatalog.productdetail.ProductVariantFinder;
import com.commercetools.sunrise.productcatalog.productdetail.SunriseProductDetailController;
import com.commercetools.sunrise.productcatalog.productdetail.view.ProductDetailPageContentFactory;
import com.commercetools.sunrise.productcatalog.productsuggestions.ProductSuggestionsControllerComponent;
import com.commercetools.sunrise.common.CommonControllerComponentSupplier;
import demo.PageHeaderControllerComponentSupplier;
import io.sphere.sdk.products.ProductProjection;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@NoCache
@RegisteredComponents({
        ProductSuggestionsControllerComponent.class,
        CommonControllerComponentSupplier.class,
        PageHeaderControllerComponentSupplier.class
})
public final class ProductDetailController extends SunriseProductDetailController {

    private final ProductReverseRouter productReverseRouter;

    @Inject
    public ProductDetailController(final TemplateRenderer templateRenderer,
                                   final ProductFinder productFinder,
                                   final ProductVariantFinder productVariantFinder,
                                   final ProductDetailPageContentFactory productDetailPageContentFactory,
                                   final ProductReverseRouter productReverseRouter) {
        super(templateRenderer, productFinder, productVariantFinder, productDetailPageContentFactory);
        this.productReverseRouter = productReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "pdp";
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