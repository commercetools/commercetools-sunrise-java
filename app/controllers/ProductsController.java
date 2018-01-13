package controllers;

import com.commercetools.sunrise.core.components.RegisteredComponents;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.models.products.ProductFetcher;
import com.commercetools.sunrise.models.products.ProductListFetcher;
import com.commercetools.sunrise.productcatalog.products.ProductRecommendationsControllerComponent;
import com.commercetools.sunrise.productcatalog.products.SunriseProductsController;

import javax.inject.Inject;

@LogMetrics
@NoCache
@RegisteredComponents(ProductRecommendationsControllerComponent.class)
public final class ProductsController extends SunriseProductsController {

    @Inject
    ProductsController(final TemplateEngine templateEngine,
                       final ProductListFetcher productListFetcher,
                       final ProductFetcher productFetcher) {
        super(templateEngine, productListFetcher, productFetcher);
    }
}