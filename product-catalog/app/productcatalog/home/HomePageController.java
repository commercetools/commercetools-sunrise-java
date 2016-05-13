package productcatalog.home;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.models.ProductDataConfig;
import common.suggestion.ProductRecommendation;
import common.template.i18n.I18nIdentifier;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;
import productcatalog.common.ProductCatalogController;
import productcatalog.common.ProductListData;
import productcatalog.common.SuggestionsData;
import productcatalog.productoverview.search.SearchConfig;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Controller for the home page.
 */
@Singleton
public class HomePageController extends ProductCatalogController {

    private final List<String> suggestionsExternalIds;
    private final int numSuggestions;

    @Inject
    public HomePageController(final ControllerDependency controllerDependency, final ProductRecommendation productRecommendation,
                              final ProductDataConfig productDataConfig, final SearchConfig searchConfig) {
        super(controllerDependency, productRecommendation, productDataConfig, searchConfig);
        this.suggestionsExternalIds = configuration().getStringList("home.suggestions.externalId", emptyList());
        this.numSuggestions = configuration().getInt("home.suggestions.count", 4);
    }

    public CompletionStage<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        return generateRecommendedProducts()
                .thenApplyAsync(recommendedProducts -> {
                    final HomePageContent pageContent = createPageContent(recommendedProducts, userContext);
                    return ok(renderHomePage(pageContent, userContext));
                }, HttpExecution.defaultContext());
    }

    protected CompletionStage<Set<ProductProjection>> generateRecommendedProducts() {
        final List<Category> suggestedCategories = suggestionsExternalIds.stream()
                .map(extId -> categoryTree().findByExternalId(extId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        if (!suggestedCategories.isEmpty()) {
            return productRecommendation().relatedToCategories(suggestedCategories, numSuggestions);
        } else {
            return completedFuture(emptySet());
        }
    }

    protected HomePageContent createPageContent(final Set<ProductProjection> recommendedProducts, final UserContext userContext) {
        final HomePageContent pageContent = new HomePageContent();
        final ProductListData productListData = new ProductListData(recommendedProducts, productDataConfig(), userContext, reverseRouter(), categoryTreeInNew());
        pageContent.setSuggestions(new SuggestionsData(productListData));
        return pageContent;
    }

    protected Html renderHomePage(final HomePageContent pageContent, final UserContext userContext) {
        pageContent.setAdditionalTitle(i18nResolver().getOrEmpty(userContext.locales(), I18nIdentifier.of("catalog:home.title")));
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx(), session());
        return templateEngine().renderToHtml("home", pageData, userContext.locales());
    }
}
