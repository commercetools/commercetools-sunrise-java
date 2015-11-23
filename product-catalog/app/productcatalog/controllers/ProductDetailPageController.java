package productcatalog.controllers;

import common.actions.LanguageFiltered;
import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.controllers.SunrisePageData;
import common.utils.PriceFormatter;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.libs.F;
import play.mvc.Result;
import productcatalog.models.*;
import productcatalog.services.CategoryService;
import productcatalog.services.ProductProjectionService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Singleton
@LanguageFiltered
public class ProductDetailPageController extends SunriseController {

    private final int numberOfSuggestions;
    private final ProductProjectionService productService;
    private final CategoryService categoryService;

    @Inject
    public ProductDetailPageController(final ControllerDependency controllerDependency, final ProductProjectionService productService, final CategoryService categoryService) {
        super(controllerDependency);
        this.productService = productService;
        this.categoryService = categoryService;
        this.numberOfSuggestions = configuration().getInt("pdp.productSuggestions.count");
    }

    public F.Promise<Result> show(final String locale, final String slug, final String sku) {
        final UserContext userContext = userContext(locale);
        final F.Promise<ProductProjection> productPromise = fetchProduct(userContext.locale(), slug);
        final F.Promise<List<ProductProjection>> suggestionPromise = productPromise.flatMap(this::fetchSuggestions);
        final F.Promise<Result> resultPromise = productPromise.flatMap(productProjection ->
                suggestionPromise.map(suggestions -> getPdpResult(userContext, suggestions, productProjection, sku)));
        return recover(resultPromise);
    }

    private F.Promise<ProductProjection> fetchProduct(final Locale locale, final String slug) {
        return productService.searchProductBySlug(locale, slug)
                .map(productOptional -> productOptional
                        .orElseThrow(() -> ProductNotFoundException.bySlug(locale, slug)));
    }

    private F.Promise<List<ProductProjection>> fetchSuggestions(final ProductProjection productProjection) {
        final List<Category> siblingCategories = categoryService.getSiblings(productProjection.getCategories());
        return productService.getSuggestions(siblingCategories, numberOfSuggestions);
    }

    private F.Promise<Result> recover(final F.Promise<Result> resultPromise) {
        return resultPromise.recover(exception -> {
            if (exception instanceof ProductNotFoundException || exception instanceof ProductVariantNotFoundException) {
                return notFoundAction();
            } else {
                throw exception;
            }
        });
    }

    private Result notFoundAction() {
        return notFound();
    }

    private Result getPdpResult(final UserContext userContext, final List<ProductProjection> suggestions,
                                final ProductProjection productProjection, final String sku) {
        final ProductVariant productVariant = getProductVariantBySku(sku, productProjection);
        final ProductDetailPageContent content = getProductDetailPageContent(userContext, suggestions, productProjection, productVariant);
        final SunrisePageData pageData = pageData(userContext, content);
        return ok(templateService().renderToHtml("pdp", pageData, userContext.locales()));
    }

    private ProductVariant getProductVariantBySku(final String sku, final ProductProjection productProjection) {
        return productProjection.findVariantBySky(sku).orElseThrow(() -> ProductVariantNotFoundException.bySku(sku));
    }

    /* Methods to build page content */

    private ProductDetailPageContent getProductDetailPageContent(final UserContext userContext,
                                                                 final List<ProductProjection> suggestions,
                                                                 final ProductProjection product,
                                                                 final ProductVariant variant) {
        final String additionalTitle = product.getName().find(userContext.locales()).orElse("");
        final ProductDetailPageContent content = new ProductDetailPageContent(additionalTitle);
        content.setProductData(new ProductData(userContext, reverseRouter(), categories(), product, variant));
        content.setBreadcrumb(new BreadcrumbData(product, variant, categories(), userContext, reverseRouter()));
        content.setShippingRates(getDeliveryData(userContext));
        content.setSuggestions(getSuggestionData(userContext, suggestions));
        content.setAddToCartFormUrl(reverseRouter().productVariantToCartForm(userContext.locale().getLanguage()).url());
        return content;
    }

    private List<ShippingRateData> getDeliveryData(final UserContext userContext) {
        final PriceFormatter priceFormatter = priceFormatter(userContext);
        final ShippingRateDataFactory shippingRateDataFactory = ShippingRateDataFactory.of(priceFormatter);
        return getShippingRates().stream()
                .map(shippingRateDataFactory::create)
                .collect(toList());
    }

    private List<ProductData> getSuggestionData(final UserContext userContext, final List<ProductProjection> suggestions) {
        return new ProductListData(userContext, reverseRouter(), categories(), suggestions).getList();
    }

    private List<ShopShippingRate> getShippingRates() {
        return emptyList();
    }
}