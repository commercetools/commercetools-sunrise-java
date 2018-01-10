package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithContent;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.products.ProductFetcher;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * Controller to show the information about a single product.
 * Loads a {@link ProductProjection} and the selected {@link ProductVariant}.
 */
public abstract class SunriseProductDetailController extends SunriseContentController implements WithContent {

    private final ProductFetcher productFetcher;

    protected SunriseProductDetailController(final ContentRenderer contentRenderer,
                                             final ProductFetcher productFetcher) {
        super(contentRenderer);
        this.productFetcher = productFetcher;
    }

    @EnableHooks
    @SunriseRoute(ProductReverseRouter.PRODUCT_DETAIL_PAGE)
    public CompletionStage<Result> show(final String productIdentifier, final String variantIdentifier) {
        return productFetcher.require(productIdentifier, variantIdentifier).thenComposeAsync(productWithVariant -> {
            final PageData pageData = PageData.of()
                    .putField("product", productWithVariant.getProduct())
                    .putField("variant", productWithVariant.getVariant());
            return okResultWithPageContent(pageData);
        }, HttpExecution.defaultContext());
    }
}