package productcatalog.controllers;

import common.cms.CmsPage;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.models.Reference;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static common.utils.PromiseUtils.combine;
import static java.util.Collections.*;
import static java.util.Collections.emptyList;
import static java.util.Locale.GERMAN;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;


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
                            final ProductOverviewPageContent content = getPopPageData(cms, result);
                            return render(view -> ok(view.productOverviewPage(content)));
                        })
        );
    }

    private ProductOverviewPageContent getPopPageData(final CmsPage cms, final List<ProductProjection> products) {
        final Translator translator = Translator.of(context().user().language(), context().user().fallbackLanguages(),
                context().project().languages());
        final PriceFormatter priceFormatter = PriceFormatter.of(context().user().country().toLocale());
        final PriceFinder priceFinder = PriceFinder.of(context().user());
        return new ProductOverviewPageContent(cms, translator, priceFormatter, priceFinder, products);
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
        final F.Promise<List<ProductProjection>> suggestionPromise = getSuggestions(product);
        final F.Promise<List<ShippingMethod>> shippingMethodsPromise = getShippingMethods();
        final List<Category> breadcrumbs = getBreadCrumbCategories(product);

        return combine(suggestionPromise, shippingMethodsPromise, (suggestions, shippingMethods) ->
                withCms("pdp", cms -> {
                    final List<ProductProjection> a = pickNRandom(suggestions, NUM_SUGGESTIONS);
                    final ProductDetailPageContent content =
                            getPdpPageData(cms, product, variant, a, shippingMethods, breadcrumbs);
                    return render(view -> ok(view.productDetailPage(content)));
                }));
    }

    private ProductDetailPageContent getPdpPageData(final CmsPage cms, final ProductProjection product,
                                                    final ProductVariant variant,
                                                    final List<ProductProjection> suggestions,
                                                    final List<ShippingMethod> shippingMethods,
                                                    final List<Category> breadcrumbs) {

        final Translator translator = Translator.of(context().user().language(), context().user().fallbackLanguages(),
                context().project().languages());
        final PriceFormatter priceFormatter = PriceFormatter.of(context().user().country().toLocale());
        final PriceFinder priceFinder = PriceFinder.of(context().user());

        return new ProductDetailPageContent(cms, translator, priceFormatter, priceFinder, product, variant,
                suggestions, shippingMethods, breadcrumbs);
    }

    private Optional<ProductVariant> findVariantBySku(final ProductProjection product, final String sku) {
        return product.getAllVariants().stream()
                .filter(variant -> variantHasSku(variant, sku))
                .findFirst();
    }

    private boolean variantHasSku(final ProductVariant variant, final String sku) {
        return variant.getSku().map(variantSku -> variantSku.equals(sku)).orElse(false);
    }

    private F.Promise<List<ShippingMethod>> getShippingMethods() {
        final ShippingMethodQuery shippingMethodQuery = ShippingMethodQuery.of();
        return sphere().execute(shippingMethodQuery).map(PagedResult::getResults);
    }

    private F.Promise<List<ProductProjection>> getSuggestions(final ProductProjection product) {
        final Optional<ProductProjectionQuery> queryOptional = product.getCategories().stream().findFirst()
                .flatMap(this::expandCategory)
                .flatMap(this::getSiblingCategories)
                .map(siblings -> ProductProjectionQuery.ofCurrent().withPredicate(p -> p.categories().isIn(siblings)));

        return queryOptional
                .map(query -> sphere().execute(query).map(PagedQueryResult::getResults))
                .orElse(F.Promise.pure(emptyList()));
    }

    private <T> List<T> pickNRandom(final List<T> elements, final int n) {
        if(elements.size() < n)
            return pickNRandom(elements, elements.size());

        final List<T> picked = new ArrayList<>();
        final Random random = new Random();

        for(int i = 0; i < n; i++)
            pick(elements, picked, random.nextInt(elements.size()));

        return picked;
    }

    private <T> void pick(final List<T> elements, final List<T> picked, int index) {
        picked.add(elements.get(index));
        elements.remove(index);
    }

    private Optional<List<Category>> getSiblingCategories(final Category category) {
        return category.getParent().map(parentRef -> categories().findByParent(parentRef));
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

    private List<Category> getBreadCrumbCategories(final ProductProjection product) {
        final Optional<Category> categoryOptional = product.getCategories().stream().findFirst()
                .flatMap(this::expandCategory);

        return categoryOptional.map(this::getCategoryWithAncestors).orElse(emptyList());
    }

    private List<Category> getCategoryWithAncestors(final Category category) {
        return concat(category.getAncestors().stream().map(this::expandCategory), Stream.of(Optional.of(category)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    private Optional<Category> expandCategory(final Reference<Category> categoryRef) {
        return categories().findById(categoryRef.getId());
    }
}