package productcatalog.controllers;

import common.cms.CmsPage;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import play.libs.F;
import play.mvc.Result;
import productcatalog.pages.*;
import productcatalog.services.CategoryService;
import productcatalog.services.ProductProjectionService;
import productcatalog.services.ShippingMethodService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

import static common.utils.PromiseUtils.combine;
import static java.util.Locale.GERMAN;

@Singleton
public class ProductCatalogController extends SunriseController {

    private static final int PAGE_SIZE = 9;
    private static final int NUM_SUGGESTIONS = 4;

    private final ProductProjectionService productService;
    private final CategoryService categoryService;
    private final ShippingMethodService shippingMethodService;

    @Inject
    public ProductCatalogController(final ControllerDependency controllerDependency, final ProductProjectionService productService, final CategoryService categoryService, final ShippingMethodService shippingMethodService) {
        super(controllerDependency);
        this.productService = productService;
        this.categoryService = categoryService;
        this.shippingMethodService = shippingMethodService;
    }

    public F.Promise<Result> pop(int page) {
        return withCms("pop", cms ->
                        productService.searchProducts(page, PAGE_SIZE).flatMap(result -> {
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
        return ProductOverviewPageContentAssembler.of(cms, translator, priceFinder, priceFormatter)
                .assemblePopContent(products);
    }

    public F.Promise<Result> pdp(final String slug, final String sku) {
        final F.Promise<Optional<ProductProjection>> productOptPromise = productService.searchProductBySlug(GERMAN, slug);

        return productOptPromise.flatMap(productOptional -> {
            final Optional<F.Promise<Result>> resultPromise = productOptional.flatMap(product ->
                    productService.findVariantBySku(product, sku).map(variant -> pdpx(product, variant)));
            return resultPromise.orElse(F.Promise.pure(notFound()));
        });
    }

    private F.Promise<Result> pdpx(final ProductProjection product, final ProductVariant variant) {
        final F.Promise<List<ProductProjection>> suggestionPromise = productService.getSuggestions(categoryService.getSiblingCategories(product));
        final F.Promise<List<ShippingMethod>> shippingMethodsPromise = shippingMethodService.getShippingMethods();
        final List<Category> breadcrumbs = categoryService.getBreadCrumbCategories(product);

        return combine(suggestionPromise, shippingMethodsPromise, (suggestions, shippingMethods) ->
                withCms("pdp", cms -> {
                    final ProductDetailPageContent content = getPageAssembler(cms)
                            .assemblePdpContent(product, variant, productService.pickNRandom(suggestions, NUM_SUGGESTIONS), shippingMethods, breadcrumbs);
                    return render(view -> ok(view.productDetailPage(content)));
                }));
    }

    private ProductDetailPageConentAssembler getPageAssembler(final CmsPage cms) {
        final Translator translator = Translator.of(context().user().language(), context().user().fallbackLanguages(), context().project().languages());
        final PriceFinder priceFinder = PriceFinder.of(context().user());
        final PriceFormatter priceFormatter = PriceFormatter.of(Locale.GERMAN);

        return ProductDetailPageConentAssembler.of(cms, translator, priceFinder, priceFormatter);
    }

    private F.Promise<Result> render(final Function<ProductCatalogView, Result> pageRenderer) {
        return withCommonCms(cms -> {
            final ProductCatalogView view = new ProductCatalogView(templateService(), context(), cms);
            return pageRenderer.apply(view);
        });
    }
}