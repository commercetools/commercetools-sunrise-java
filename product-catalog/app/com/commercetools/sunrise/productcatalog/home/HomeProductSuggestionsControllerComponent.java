package com.commercetools.sunrise.productcatalog.home;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.common.suggestion.ProductRecommendation;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.hooks.events.RequestStartedHook;
import com.commercetools.sunrise.productcatalog.common.ProductListBean;
import com.commercetools.sunrise.productcatalog.common.ProductListBeanFactory;
import com.commercetools.sunrise.productcatalog.common.SuggestionsBean;
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

    private List<String> suggestionsExternalIds;
    private int numSuggestions;
    private Set<ProductProjection> recommendedProducts;

    @Inject
    private ProductListBeanFactory productListBeanFactory;
    @Inject
    private ProductRecommendation productRecommendation;
    @Inject
    private CategoryTree categoryTree;

    @Inject
    public void setConfiguration(final Configuration configuration) {
        this.suggestionsExternalIds = configuration.getStringList("homeSuggestions.externalId", emptyList());
        this.numSuggestions = configuration.getInt("homeSuggestions.count", 4);
    }


    @Override
    public CompletionStage<?> onRequestStarted(final Http.Context context) {
        final List<Category> suggestedCategories = suggestionsExternalIds.stream()
                .map(extId -> categoryTree.findByExternalId(extId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        return getRecommendedProducts(suggestedCategories)
                .thenAccept(recommendedProducts -> this.recommendedProducts = recommendedProducts);
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

    @Override
    public void onPageDataReady(final PageData pageData) {
        if (pageData.getContent() instanceof HomePageContent) {
            final HomePageContent content = (HomePageContent) pageData.getContent();
            final ProductListBean productListBean = productListBeanFactory.create(recommendedProducts);
            content.setSuggestions(new SuggestionsBean(productListBean));
        }
    }
}
