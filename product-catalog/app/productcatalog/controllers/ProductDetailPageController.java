package productcatalog.controllers;

import common.actions.LanguageFiltered;
import common.cms.CmsPage;
import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.pages.*;
import common.utils.PriceFormatter;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.libs.F;
import play.mvc.Result;
import productcatalog.models.ProductNotFoundException;
import productcatalog.models.ProductVariantNotFoundException;
import productcatalog.models.ShopShippingRate;
import productcatalog.pages.*;
import productcatalog.services.CategoryService;
import productcatalog.services.ProductProjectionService;
import productcatalog.services.ShippingMethodService;

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
    private final ShippingMethodService shippingMethodService;

    @Inject
    public ProductDetailPageController(final ControllerDependency controllerDependency, final ProductProjectionService productService, final CategoryService categoryService, final ShippingMethodService shippingMethodService) {
        super(controllerDependency);
        this.productService = productService;
        this.categoryService = categoryService;
        this.shippingMethodService = shippingMethodService;
        this.numberOfSuggestions = configuration().getInt("pdp.productSuggestions.count");
    }

    public F.Promise<Result> show(final String locale, final String slug, final String sku) {
        final UserContext userContext = userContext(locale);
        final F.Promise<CmsPage> cmsPagePromise = cmsService().getPage(userContext.locale(), "pdp");
        final F.Promise<ProductProjection> productProjectionPromise = fetchProduct(userContext.locale(), slug);
        final F.Promise<List<ProductProjection>> suggestionPromise = productProjectionPromise.flatMap(this::fetchSuggestions);
        final F.Promise<Result> resultPromise = productProjectionPromise.flatMap(productProjection ->
                cmsPagePromise.flatMap(cms ->
                        suggestionPromise.map(suggestions ->
                                getPdpResult(userContext, cms, suggestions, productProjection, sku))));
        return recover(resultPromise);
    }

    private F.Promise<ProductProjection> fetchProduct(final Locale locale, final String slug) {
        return productService.searchProductBySlug(locale, slug)
                .map(productOptional -> productOptional
                        .orElseThrow(() -> ProductNotFoundException.bySlug(locale, slug)));
    }

    private F.Promise<List<ProductProjection>> fetchSuggestions(final ProductProjection productProjection) {
        final List<Category> siblingCategories = categoryService.getSiblingCategories(productProjection.getCategories());
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

    private Result getPdpResult(final UserContext userContext, final CmsPage cms, final List<ProductProjection> suggestions,
                                final ProductProjection productProjection, final String sku) {
        final ProductVariant productVariant = getProductVariantBySku(sku, productProjection);
        final ProductDetailPageContent content = getProductDetailPageContent(userContext, cms, suggestions, productProjection, productVariant);
        final SunrisePageData pageData = pageData(userContext, content);
        return ok(templateService().renderToHtml("pdp", pageData, userContext.locales()));
    }

    private ProductVariant getProductVariantBySku(final String sku, final ProductProjection productProjection) {
        return productProjection.findVariantBySky(sku).orElseThrow(() -> ProductVariantNotFoundException.bySku(sku));
    }

    /* Methods to build page content */

    private ProductDetailPageContent getProductDetailPageContent(final UserContext userContext, final CmsPage cms,
                                                                 final List<ProductProjection> suggestions,
                                                                 final ProductProjection productProjection,
                                                                 final ProductVariant productVariant) {
        final String additionalTitle = productProjection.getName().find(userContext.locales()).orElse("");
        final PdpStaticData staticData = getStaticData(cms);
        final List<SelectableLinkData> breadcrumbData = getBreadcrumbData(userContext, productProjection);
        final ProductData productData = getProductData(userContext, productProjection, productVariant);
        final List<ShippingRateData> deliveryData = getDeliveryData(userContext);
        final List<ProductData> suggestionData = getSuggestionData(userContext, suggestions);
        final String formAction = reverseRouter().productVariantToCartForm(userContext.locale().getLanguage()).url();
        return new ProductDetailPageContent(additionalTitle, staticData, breadcrumbData, productData, deliveryData, suggestionData, formAction);
    }

    private PdpStaticData getStaticData(final CmsPage cms) {
        return new PdpStaticData(cms, BagItemDataFactory.of().create(100), RatingDataFactory.of(cms).create());
    }

    private List<SelectableLinkData> getBreadcrumbData(final UserContext userContext, final ProductProjection productProjection) {
        final BreadcrumbDataFactory breadcrumbDataFactory = BreadcrumbDataFactory.of(reverseRouter(), userContext.locale());
        final List<Category> breadcrumbCategories = getBreadcrumbsForProduct(productProjection);
        return breadcrumbDataFactory.create(breadcrumbCategories);
    }

    private List<Category> getBreadcrumbsForProduct(final ProductProjection product) {
        return product.getCategories().stream().findFirst()
                .map(categoryService::getBreadCrumbCategories)
                .orElse(emptyList());
    }

    private ProductData getProductData(final UserContext userContext, final ProductProjection productProjection,
                                       final ProductVariant productVariant) {
        final ProductDataFactory productDataFactory = ProductDataFactory.of(userContext, reverseRouter(), categoryService);
        return productDataFactory.create(productProjection, productVariant);
    }

    private List<ShippingRateData> getDeliveryData(final UserContext userContext) {
        final PriceFormatter priceFormatter = priceFormatter(userContext);
        final ShippingRateDataFactory shippingRateDataFactory = ShippingRateDataFactory.of(priceFormatter);
        return getShippingRates().stream()
                .map(shippingRateDataFactory::create)
                .collect(toList());
    }

    private List<ProductData> getSuggestionData(final UserContext userContext, final List<ProductProjection> suggestions) {
        final ProductDataFactory productDataFactory = ProductDataFactory.of(userContext, reverseRouter(), categoryService);
        return suggestions.stream()
                .map((product) -> productDataFactory.create(product, product.getMasterVariant()))
                .collect(toList());
    }

    private List<ShopShippingRate> getShippingRates() {
        return emptyList();
    }
}