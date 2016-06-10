package com.commercetools.sunrise.productcatalog.home;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.SunrisePageData;
import com.commercetools.sunrise.common.hooks.RequestHook;
import com.commercetools.sunrise.common.hooks.SunrisePageDataHook;
import com.commercetools.sunrise.common.suggestion.ProductRecommendation;
import com.commercetools.sunrise.framework.ControllerComponent;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;
import play.mvc.Http;
import com.commercetools.sunrise.productcatalog.common.ProductListBean;
import com.commercetools.sunrise.productcatalog.common.ProductListBeanFactory;
import com.commercetools.sunrise.productcatalog.common.SuggestionsData;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.concurrent.CompletableFuture.completedFuture;

public class HomeProductSuggestionsControllerComponent implements ControllerComponent, RequestHook, SunrisePageDataHook {
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
    private UserContext userContext;

    @Inject
    public void setConfiguration(final Configuration configuration) {
        this.suggestionsExternalIds = configuration.getStringList("homeSuggestions.externalId", emptyList());
        this.numSuggestions = configuration.getInt("homeSuggestions.count", 4);
    }


    @Override
    public CompletionStage<?> onRequest(final Http.Context context) {
        final List<Category> suggestedCategories = suggestionsExternalIds.stream()
                .map(extId -> categoryTree.findByExternalId(extId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        if (!suggestedCategories.isEmpty()) {
            return productRecommendation.relatedToCategories(suggestedCategories, numSuggestions, userContext)
                    .exceptionally(e -> {
                        logger.error("failed to fetch product suggestions", e);
                        return emptySet();
                    })
                    .thenAccept(suggestedProducts -> this.recommendedProducts = suggestedProducts);
        } else {
            return completedFuture(emptySet());
        }
    }

    @Override
    public void acceptSunrisePageData(final SunrisePageData sunrisePageData) {
        if (sunrisePageData.getContent() instanceof HomePageContent) {
            final HomePageContent content = (HomePageContent) sunrisePageData.getContent();
            final ProductListBean productListBean = productListBeanFactory.create(recommendedProducts);
            content.setSuggestions(new SuggestionsData(productListBean));
        }
    }
}
