package productcatalog.home;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.models.ProductDataConfig;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import play.libs.concurrent.HttpExecution;
import play.mvc.Controller;
import play.mvc.Result;
import productcatalog.common.ProductCatalogController;
import productcatalog.common.ProductListData;
import productcatalog.common.SuggestionsData;
import productcatalog.productoverview.search.SearchConfig;
import common.suggestion.ProductSuggestion;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

/**
 * Controller for the home page.
 */
@Singleton
public class HomeController extends ProductCatalogController {

    private final List<String> suggestionsExternalIds;
    private final int numSuggestions;


    @Inject
    public HomeController(final ControllerDependency controllerDependency, final ProductSuggestion productSuggestion,
                          final ProductDataConfig productDataConfig, final SearchConfig searchConfig) {
        super(controllerDependency, productSuggestion, productDataConfig, searchConfig);
        this.suggestionsExternalIds = configuration().getStringList("home.suggestions.externalId", emptyList());
        this.numSuggestions = configuration().getInt("home.suggestions.count", 4);
    }

    public CompletionStage<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final List<Category> suggestedCategories = suggestedCategories();
        if (!suggestedCategories.isEmpty()) {
            return productSuggestion().relatedToCategories(suggestedCategories, numSuggestions)
                    .thenComposeAsync(suggestions -> renderHome(userContext, suggestions), HttpExecution.defaultContext());
        } else {
            return renderHome(userContext, emptySet());
        }
    }

    private CompletionStage<Result> renderHome(final UserContext userContext, final Set<ProductProjection> suggestions) {
        final HomePageContent homePageContent = new HomePageContent();
        homePageContent.setSuggestions(createSuggestions(userContext, suggestions));
        return renderPage("home", homePageContent, userContext, ctx(), session()).thenApply(Controller::ok);
    }

    private List<Category> suggestedCategories() {
        return suggestionsExternalIds.stream()
                .map(extId -> categoryTree().findByExternalId(extId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private SuggestionsData createSuggestions(final UserContext userContext, final Set<ProductProjection> suggestions) {
        final ProductListData productListData = new ProductListData(suggestions, productDataConfig(), userContext, reverseRouter(), categoryTreeInNew());
        return new SuggestionsData(productListData);
    }

}
