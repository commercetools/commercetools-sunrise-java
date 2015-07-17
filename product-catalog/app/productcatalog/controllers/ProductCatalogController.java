package productcatalog.controllers;

import common.contexts.AppContext;
import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.prices.PriceFinder;
import common.utils.PriceFormatterImpl;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.queries.PagedResult;
import play.libs.F;
import play.mvc.Result;
import productcatalog.pages.ProductCatalogView;
import productcatalog.pages.ProductDetailPageContent;
import productcatalog.pages.ProductOverviewPageContent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Locale.GERMAN;

@Singleton
public class ProductCatalogController extends SunriseController {
    private static final int PAGE_SIZE = 9;
    private static final int NUM_SUGGESTIONS = 4;

    @Inject
    public ProductCatalogController(final ControllerDependency controllerDependency) {
        super(controllerDependency);
    }

    public F.Promise<Result> pop(int page) {
        return withCms("pop", cms ->
            searchProducts(page).flatMap(result -> {
                final ProductOverviewPageContent content = new ProductOverviewPageContent(cms, context(), result, PriceFormatterImpl.of());
                return render(view -> ok(view.productOverviewPage(content)));
            })
        );
    }

    public F.Promise<Result> pdp(final String slug) {
        final F.Promise<Optional<ProductProjection>> productOptPromise = searchProductBySlug(slug);
        final F.Promise<List<ProductProjection>> suggestionsPromise = getSuggestions();

        final F.Promise<F.Tuple<Optional<ProductProjection>, List<ProductProjection>>> zip = productOptPromise.zip(suggestionsPromise);

        return withCms("pdp", cms -> zip.flatMap(tuple -> {
                    final Optional<ProductProjection> productOpt = tuple._1;
                    final List<ProductProjection> suggestions = tuple._2;

                    return productOpt.map(product -> {
                        final ProductDetailPageContent content =
                                new ProductDetailPageContent(cms, context(), product, suggestions,
                                        PriceFinder.of(context().user()), PriceFormatterImpl.of());
                        return render(view -> ok(view.productDetailPage(content)));
                    }).orElse(F.Promise.pure(notFound()));
                })
        );
    }

    private F.Promise<List<ProductProjection>> getSuggestions() {
        return sphere().execute(ProductProjectionSearch.ofCurrent().withLimit(NUM_SUGGESTIONS))
                .map(PagedResult::getResults);
    }

    private F.Promise<Optional<ProductProjection>> searchProductBySlug(final String slug) {
        return sphere().execute(ProductProjectionQuery.ofCurrent().bySlug(GERMAN, slug)).map(PagedQueryResult::head);
    }

    private F.Promise<List<ProductProjection>> searchProducts(final int page) {
        final int offset = (page - 1) * PAGE_SIZE;
        return sphere().execute(ProductProjectionSearch.ofCurrent().withOffset(offset).withLimit(PAGE_SIZE))
                .map(PagedResult::getResults);
    }

    private F.Promise<Result> render(final Function<ProductCatalogView, Result> pageRenderer) {
        return withCommonCms(cms -> {
            final ProductCatalogView view = new ProductCatalogView(templateService(), context(), cms);
            return pageRenderer.apply(view);
        });
    }
}