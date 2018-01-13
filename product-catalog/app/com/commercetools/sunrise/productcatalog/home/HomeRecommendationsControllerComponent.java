package com.commercetools.sunrise.productcatalog.home;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.HttpRequestStartedHook;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.recommendations.ProductRecommender;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;

public final class HomeRecommendationsControllerComponent implements ControllerComponent, HttpRequestStartedHook, PageDataHook {

    private static final Logger logger = LoggerFactory.getLogger(HomeRecommendationsControllerComponent.class);

    private final int numSuggestions;
    private final List<String> suggestionsExternalIds;
    private final ProductRecommender productRecommender;
    private final CategoryTree categoryTree;

    private CompletionStage<List<ProductProjection>> recommendedProductsStage;

    @Inject
    public HomeRecommendationsControllerComponent(final Configuration configuration,
                                                  final ProductRecommender productRecommender, final CategoryTree categoryTree) {
        this.suggestionsExternalIds = configuration.getStringList("homeSuggestions.externalId", emptyList());
        this.numSuggestions = configuration.getInt("homeSuggestions.count", 4);
        this.productRecommender = productRecommender;
        this.categoryTree = categoryTree;
    }


    @Override
    public CompletionStage<?> onHttpRequestStarted(final Http.Context context) {
        final List<Category> suggestedCategories = suggestionsExternalIds.stream()
                .map(categoryTree::findByExternalId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        this.recommendedProductsStage = getRecommendedProducts(suggestedCategories);
        return completedFuture(null);
    }

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        if (recommendedProductsStage != null) {
            return recommendedProductsStage.thenApply(products -> pageData.put("recommendedProducts", products));
        }
        return completedFuture(pageData);
    }

    private CompletionStage<List<ProductProjection>> getRecommendedProducts(final List<Category> suggestedCategories) {
        if (!suggestedCategories.isEmpty()) {
            return productRecommender.relatedToCategories(suggestedCategories, numSuggestions)
                    .exceptionally(e -> {
                        logger.error("Failed to fetch product suggestions", e);
                        return emptyList();
                    });
        } else {
            return completedFuture(emptyList());
        }
    }
}
