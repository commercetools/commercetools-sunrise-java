package com.commercetools.sunrise.productcatalog.home;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.common.suggestion.ProductRecommendation;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.framework.hooks.events.RequestStartedHook;
import com.commercetools.sunrise.productcatalog.common.ProductListBean;
import com.commercetools.sunrise.productcatalog.common.ProductListBeanFactory;
import com.commercetools.sunrise.productcatalog.common.SuggestionsBean;
import com.commercetools.sunrise.productcatalog.home.view.HomePageContent;
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
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.concurrent.CompletableFuture.completedFuture;

public class HomeProductSuggestionsControllerComponent implements ControllerComponent, RequestStartedHook, PageDataReadyHook {

    private static final Logger logger = LoggerFactory.getLogger(HomeProductSuggestionsControllerComponent.class);

    private final int numSuggestions;
    private final List<String> suggestionsExternalIds;
    private final ProductListBeanFactory productListBeanFactory;
    private final ProductRecommendation productRecommendation;
    private final CategoryTree categoryTree;

    private Set<ProductProjection> recommendedProducts;

    @Inject
    public HomeProductSuggestionsControllerComponent(final Configuration configuration, final ProductListBeanFactory productListBeanFactory,
                                                     final ProductRecommendation productRecommendation, final CategoryTree categoryTree) {
        this.suggestionsExternalIds = configuration.getStringList("homeSuggestions.externalId", emptyList());
        this.numSuggestions = configuration.getInt("homeSuggestions.count", 4);
        this.productListBeanFactory = productListBeanFactory;
        this.productRecommendation = productRecommendation;
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
            final ProductListBean productListBean = productListBeanFactory.create(recommendedProducts);
            content.setSuggestions(new SuggestionsBean(productListBean));
        }
    }

    private CompletionStage<Set<ProductProjection>> getRecommendedProducts(final List<Category> suggestedCategories) {
        if (!suggestedCategories.isEmpty()) {
            return productRecommendation.relatedToCategories(suggestedCategories, numSuggestions)
                    .exceptionally(e -> {
                        logger.error("Failed to fetch product suggestions", e);
                        return emptySet();
                    });
        } else {
            return completedFuture(emptySet());
        }
    }
}
