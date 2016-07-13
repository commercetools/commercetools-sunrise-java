package com.commercetools.sunrise.productcatalog;

import com.commercetools.sunrise.common.DefaultTestModule;
import com.commercetools.sunrise.common.controllers.TestableReverseRouter;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.suggestion.ProductRecommendation;
import com.google.inject.name.Names;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.utils.CompletableFutureUtils.exceptionallyCompletedFuture;
import static java.util.Collections.emptyList;

public class ProductCatalogTestModule extends DefaultTestModule {

    @Override
    protected void configure() {
        super.configure();
        bind(ProductDataConfig.class).toInstance(ProductDataConfig.of(null, emptyList(), emptyList(), emptyList()));
        bind(CategoryTree.class).toInstance(CategoryTree.of(emptyList()));
        bind(CategoryTree.class).annotatedWith(Names.named("new")).toInstance(CategoryTree.of(emptyList()));
        bind(HomeReverseRouter.class).toInstance(new TestableReverseRouter());
        bind(ProductReverseRouter.class).toInstance(new TestableReverseRouter());
        bind(ProductRecommendation.class).toInstance(unsupportedProductRecommendation());
    }

    private ProductRecommendation unsupportedProductRecommendation() {
        return new ProductRecommendation() {
            @Override
            public CompletionStage<Set<ProductProjection>> relatedToProduct(final ProductProjection product, final int numProducts) {
                return exceptionallyCompletedFuture(new UnsupportedOperationException());
            }

            @Override
            public CompletionStage<Set<ProductProjection>> relatedToCategories(final List<Category> categories, final int numProducts) {
                return exceptionallyCompletedFuture(new UnsupportedOperationException());
            }
        };
    }
}
