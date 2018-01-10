package controllers.productcatalog;

import com.commercetools.sunrise.core.components.RegisteredComponents;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.models.products.ProductFetcher;
import com.commercetools.sunrise.productcatalog.productdetail.ProductProjectionRecommendationsControllerComponent;
import com.commercetools.sunrise.productcatalog.productdetail.SunriseProductDetailController;
import com.commercetools.sunrise.productcatalog.productdetail.viewmodels.ProductDetailPageContentFactory;

import javax.inject.Inject;

@LogMetrics
@NoCache
@RegisteredComponents(ProductProjectionRecommendationsControllerComponent.class)
public final class ProductDetailController extends SunriseProductDetailController {

    @Inject
    public ProductDetailController(final ContentRenderer contentRenderer,
                                   final ProductFetcher productFinder,
                                   final ProductDetailPageContentFactory pageContentFactory) {
        super(contentRenderer, productFinder, pageContentFactory);
    }

    @Override
    public String getTemplateName() {
        return "pdp";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }
}