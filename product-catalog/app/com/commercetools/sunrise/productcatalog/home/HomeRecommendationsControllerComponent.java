package com.commercetools.sunrise.productcatalog.home;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.common.suggestion.ProductRecommender;
import com.commercetools.sunrise.framework.components.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.framework.hooks.events.RequestStartedHook;
import com.commercetools.sunrise.productcatalog.home.viewmodels.HomePageContent;
import com.commercetools.sunrise.productcatalog.productoverview.viewmodels.ProductListBeanFactory;
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

public final class HomeRecommendationsControllerComponent implements ControllerComponent, RequestStartedHook, PageDataReadyHook {

    private static final Logger logger = LoggerFactory.getLogger(HomeRecommendationsControllerComponent.class);

    private final int numSuggestions;
    private final List<String> suggestionsExternalIds;
    private final ProductListBeanFactory productListBeanFactory;
    private final ProductRecommender productRecommender;
    private final CategoryTree categoryTree;

    private List<ProductProjection> recommendedProducts;

    @Inject
    public HomeRecommendationsControllerComponent(final Configuration configuration, final ProductListBeanFactory productListBeanFactory,
                                                  final ProductRecommender productRecommender, final CategoryTree categoryTree) {
        this.suggestionsExternalIds = configuration.getStringList("homeSuggestions.externalId", emptyList());
        this.numSuggestions = configuration.getInt("homeSuggestions.count", 4);
        this.productListBeanFactory = productListBeanFactory;
        this.productRecommender = productRecommender;
        this.categoryTree = categoryTree;
    }


    @Override
    public CompletionStage<?> onRequestStarted(final Http.Context context) {
        final List<Category> suggestedCategories = suggestionsExternalIds.stream()
                .map(categoryTree::findByExternalId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        return getRecommendedProducts(suggestedCategories)
                .thenAccept(recommendedProducts -> this.recommendedProducts = recommendedProducts);
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        if (recommendedProducts != null && pageData.getContent() instanceof HomePageContent) {
            final HomePageContent content = (HomePageContent) pageData.getContent();
            content.setSuggestions(productListBeanFactory.create(recommendedProducts));
        }
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
