package com.commercetools.sunrise.productcatalog;

import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.suggestion.ProductRecommendation;
import com.commercetools.sunrise.pt.DefaultTestModule;
import com.commercetools.sunrise.test.TestableCall;
import com.google.inject.name.Names;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;
import play.mvc.Call;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.utils.CompletableFutureUtils.exceptionallyCompletedFuture;
import static java.util.Collections.emptyList;

public class ProductCatalogTestModule extends DefaultTestModule {

    @Override
    protected void configure() {
        super.configure();
        bind(CategoryTree.class).toInstance(CategoryTree.of(emptyList()));
        bind(CategoryTree.class).annotatedWith(Names.named("new")).toInstance(CategoryTree.of(emptyList()));
        bind(HomeReverseRouter.class).toInstance(homeReverseRouter());
        bind(ProductReverseRouter.class).toInstance(productReverseRouter());
        bind(ProductRecommendation.class).toInstance(unsupportedProductRecommendation());
    }

    private HomeReverseRouter homeReverseRouter() {
        return new HomeReverseRouter() {
            @Override
            public Call homePageCall(final String languageTag) {
                return new TestableCall("/");
            }

            @Override
            public String languageTag() {
                return "en";
            }

            @Override
            public Locale locale() {
                return Locale.ENGLISH;
            }
        };
    }

    private ProductReverseRouter productReverseRouter() {
        return new ProductReverseRouter() {
            @Override
            public String languageTag() {
                return "en";
            }

            @Override
            public Locale locale() {
                return Locale.ENGLISH;
            }

            @Override
            public Call productDetailPageCall(final String languageTag, final String productSlug, final String sku) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Call productOverviewPageCall(final String languageTag, final String categorySlug) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Call processSearchProductsForm(final String languageTag) {
                throw new UnsupportedOperationException();
            }
        };
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
