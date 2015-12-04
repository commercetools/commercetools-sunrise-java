package productcatalog.controllers;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.models.ProductDataConfig;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import play.libs.F;
import play.mvc.Result;
import play.twirl.api.Html;
import productcatalog.models.ProductListData;
import productcatalog.models.SuggestionsData;
import productcatalog.services.ProductService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
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

    public F.Promise<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final HomePageContent content = new HomePageContent();
        final List<Category> suggestedCategories = suggestedCategories();
        if (!suggestedCategories.isEmpty()) {
            return productService().getSuggestions(suggestedCategories, numSuggestions).map(suggestions -> {
                content.setSuggestions(createSuggestions(userContext, suggestions));
                return ok(renderHome(userContext, content));
            });
        } else {
            return  F.Promise.pure(ok(renderHome(userContext, content)));
        }
    }

    private Html renderHome(final UserContext userContext, final HomePageContent content) {
        return templateService().renderToHtml("home", pageData(userContext, content, ctx()), userContext.locales());
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
