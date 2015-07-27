package productcatalog.controllers;

import common.cms.CmsPage;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.prices.PriceFinder;
import common.utils.PriceFormatterImpl;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.queries.PagedResult;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.shippingmethods.queries.ShippingMethodQuery;
import play.libs.F;
import play.mvc.Result;
import productcatalog.pages.ProductCatalogView;
import productcatalog.pages.ProductDetailPageContent;
import productcatalog.pages.ProductOverviewPageContent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.BiFunction;
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

    public F.Promise<Result> pdp(final String slug, final String sku) {
        final F.Promise<Optional<ProductProjection>> productOptPromise = searchProductBySlug(GERMAN, slug);

        return productOptPromise.flatMap(productOptional -> {
            final Optional<F.Promise<Result>> resultPromise = productOptional.flatMap(product ->
                    findVariantBySku(product, sku).map(variant -> pdpx(product, variant)));
            return resultPromise.orElse(F.Promise.pure(notFound()));
        });
    }

    private F.Promise<Result> pdpx(final ProductProjection product, final ProductVariant variant) {
        final F.Promise<List<ProductProjection>> suggestionPromise = getSuggestions(product, NUM_SUGGESTIONS);
        final F.Promise<List<ShippingMethod>> shippingMethodsPromise = getShippingMethods();

        return combine(suggestionPromise, shippingMethodsPromise, (List<ProductProjection> suggestions, List<ShippingMethod> shippingMethods) -> withCms("pdp", cms -> {
            final ProductDetailPageContent content = getPdpPageData(cms, product, variant, suggestions, shippingMethods);
            return render(view -> ok(view.productDetailPage(content)));
        }));
    }

    private ProductDetailPageContent getPdpPageData(final CmsPage cms, final ProductProjection product, final ProductVariant variant, final List<ProductProjection> suggestions, final List<ShippingMethod> shippingMethods) {
            return new ProductDetailPageContent(cms, context(), categories(), product, variant, suggestions, shippingMethods, PriceFormatterImpl.of(), PriceFinder.of(context().user()));
    }

    private Optional<ProductVariant> findVariantBySku(final ProductProjection product, final String sku) {
        return product.getAllVariants().stream()
                .filter(variant -> variantHasSku(variant, sku))
                .findFirst();
    }

    private boolean variantHasSku(final ProductVariant variant, final String sku) {
        return variant.getSku().map(variantSku -> variantSku.equals(sku)).orElse(false);
    }

    private static <A, B, R> F.Promise<R> combine(final F.Promise<A> aPromise, final F.Promise<B> bPromise, final BiFunction<A, B, F.Promise<R>> function) {
        return aPromise.zip(bPromise).flatMap(tuple -> function.apply(tuple._1, tuple._2));
    }

    private F.Promise<List<ShippingMethod>> getShippingMethods() {
        final ShippingMethodQuery shippingMethodQuery = ShippingMethodQuery.of();
        return sphere().execute(shippingMethodQuery).map(PagedResult::getResults);
    }

    private F.Promise<List<ProductProjection>> getSuggestions(final ProductProjection product, final int num) {
        final Optional<Category> categoryOpt = product.getCategories().stream().findFirst().flatMap(ref -> categories().findById(ref.getId()));

        final Optional<ProductProjectionQuery> queryOpt = categoryOpt.flatMap(category -> category.getParent().map(parentRef -> {
            final List<Category> siblings = categories().findByParent(parentRef);
            return ProductProjectionQuery.ofCurrent().withPredicate(p -> p.categories().isIn(siblings));
        }));

        return queryOpt.map(query -> sphere().execute(query.withLimit(num))
                .map(PagedQueryResult::getResults))
                .orElse(F.Promise.pure(Collections.emptyList()));
    }

    private F.Promise<Optional<ProductProjection>> searchProductBySlug(final Locale locale, final String slug) {
        return sphere().execute(ProductProjectionQuery.ofCurrent().bySlug(locale, slug))
                .map(PagedQueryResult::head);
    }

    private F.Promise<List<ProductProjection>> searchProducts(final int page) {
        final int offset = (page - 1) * PAGE_SIZE;
        return sphere().execute(ProductProjectionSearch.ofCurrent()
                .withOffset(offset)
                .withLimit(PAGE_SIZE))
                .map(PagedResult::getResults);
    }

    private F.Promise<Result> render(final Function<ProductCatalogView, Result> pageRenderer) {
        return withCommonCms(cms -> {
            final ProductCatalogView view = new ProductCatalogView(templateService(), context(), cms);
            return pageRenderer.apply(view);
        });
    }
}