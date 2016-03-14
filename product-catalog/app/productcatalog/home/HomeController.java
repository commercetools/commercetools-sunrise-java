package productcatalog.home;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.models.ProductDataConfig;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;
import productcatalog.common.ProductCatalogController;
import productcatalog.common.ProductListData;
import productcatalog.common.SuggestionsData;
import productcatalog.services.ProductService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

/**
 * Controller for the home page.
 */
@Singleton
public class HomeController extends ProductCatalogController {
    private final List<String> suggestionsExternalIds;
    private final int numSuggestions;


    @Inject
    public HomeController(final ControllerDependency controllerDependency, final ProductService productService,
                          final ProductDataConfig productDataConfig) {
        super(controllerDependency, productService, productDataConfig);
        this.suggestionsExternalIds = configuration().getStringList("home.suggestions.externalId", emptyList());
        this.numSuggestions = configuration().getInt("home.suggestions.count", 4);
    }

    public CompletionStage<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final List<Category> suggestedCategories = suggestedCategories();
        if (!suggestedCategories.isEmpty()) {
            return productService().getSuggestions(suggestedCategories, numSuggestions).thenApplyAsync(suggestions ->
                    ok(renderHome(userContext, suggestions)), HttpExecution.defaultContext());
        } else {
            return CompletableFuture.completedFuture(ok(renderHome(userContext, emptyList())));
        }
    }

    private HomePageContent createPageContent(final UserContext userContext, final List<ProductProjection> suggestions) {
        final HomePageContent homePageContent = new HomePageContent();
        homePageContent.setSuggestions(createSuggestions(userContext, suggestions));
        return homePageContent;
    }

    private Html renderHome(final UserContext userContext, final List<ProductProjection> suggestions) {
        final HomePageContent pageContent = createPageContent(userContext, suggestions);
        return templateService().renderToHtml("home", pageData(userContext, pageContent, ctx(), session()), userContext.locales());
    }

    private List<Category> suggestedCategories() {
        return suggestionsExternalIds.stream()
                .map(extId -> categoryTree().findByExternalId(extId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private SuggestionsData createSuggestions(final UserContext userContext, final List<ProductProjection> suggestions) {
        final ProductListData productListData = new ProductListData(suggestions, productDataConfig(), userContext, reverseRouter(), categoryTreeInNew());
        return new SuggestionsData(productListData);
    }

}
