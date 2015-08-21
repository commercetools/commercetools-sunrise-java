package productcatalog.controllers;

import common.cms.CmsPage;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.pages.*;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.Configuration;
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
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

@Singleton
public class ProductDetailPageController extends SunriseController {

    private final int numberOfSuggestions;
    private final ProductProjectionService productService;
    private final CategoryService categoryService;
    private final ShippingMethodService shippingMethodService;

    @Inject
    public ProductDetailPageController(final Configuration configuration, final ControllerDependency controllerDependency, final ProductProjectionService productService, final CategoryService categoryService, final ShippingMethodService shippingMethodService) {
        super(controllerDependency);
        this.productService = productService;
        this.categoryService = categoryService;
        this.shippingMethodService = shippingMethodService;
        this.numberOfSuggestions = configuration.getInt("pdp.productSuggestions.count");
    }

    public F.Promise<Result> show(final String language, final String slug, final String sku) {
        final F.Promise<ProductProjection> productProjectionPromise = fetchProduct(language, slug);
        final F.Promise<List<ProductProjection>> suggestionPromise = productProjectionPromise.flatMap(this::getSuggestions);
        final F.Promise<CmsPage> cmsPagePromise = getCmsPage("pdp");
        final F.Promise<CmsPage> commonCmsPagePromise = getCommonCmsPage();

        final F.Promise<Result> resultPromise = productProjectionPromise.flatMap(productProjection ->
                cmsPagePromise.flatMap(cms ->
                        commonCmsPagePromise.flatMap(commonCmsPage ->
                                suggestionPromise.map(suggestions ->
                                        getPdpResult(commonCmsPage, cms, suggestions, productProjection, sku)))));

        return recover(resultPromise);
    }

    private F.Promise<List<ProductProjection>> getSuggestions(final ProductProjection productProjection) {
        return productService.getSuggestions(categoryService.getSiblingCategories(productProjection.getCategories()), numberOfSuggestions);
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

    private Result getPdpResult(final CmsPage commonCmsPage, final CmsPage cms, final List<ProductProjection> suggestions, final ProductProjection productProjection, final String sku) {
        final ProductVariant productVariant = obtainProductVariantBySku(sku, productProjection);
        final ProductDetailPageContent content = getProductDetailPageContent(cms, suggestions, productProjection, productVariant);
        final ProductCatalogView view = new ProductCatalogView(templateService(), context(), commonCmsPage);
        return ok(view.productDetailPage(content));
    }

    private ProductDetailPageContent getProductDetailPageContent(final CmsPage cms, final List<ProductProjection> suggestions, final ProductProjection productProjection, final ProductVariant productVariant) {
        final String additionalTitle = getTranslator().findTranslation(productProjection.getName());
        final PdpStaticData staticData = getStaticData(cms);
        final List<LinkData> breadcrumbData = getBreadcrumbData(productProjection);
        final List<ImageData> galleryData = getGalleryData(productVariant);
        final ProductData productData = getProductData(productProjection, productVariant);
        final List<ProductThumbnailData> suggestionData = suggestionsToViewData(suggestions);
        final List<ShippingRateData> deliveryData = getDeliveryData();
        return new ProductDetailPageContent(additionalTitle, staticData, breadcrumbData, galleryData, productData, deliveryData, suggestionData);
    }

    private List<ShippingRateData> getDeliveryData() {
        final ShippingRateDataFactory shippingRateDataFactory = ShippingRateDataFactory.of(getPriceFormatter());
        return getShippingRates().stream().map(shippingRateDataFactory::create).collect(toList());
    }

    private ProductData getProductData(final ProductProjection productProjection, final ProductVariant productVariant) {
        return ProductDataFactory.of(getTranslator(), getPriceFinder(), getPriceFormatter())
                .create(productProjection, productVariant);
    }

    private PdpStaticData getStaticData(final CmsPage cms) {
        return new PdpStaticData(cms, BagItemDataFactory.of().create(100), RatingDataFactory.of(cms).create());
    }

    private List<ImageData> getGalleryData(final ProductVariant productVariant) {
        return productVariant.getImages().stream().map(ImageData::of).collect(toList());
    }

    private List<LinkData> getBreadcrumbData(final ProductProjection productProjection) {
        final CategoryLinkDataFactory categoryLinkDataFactory = CategoryLinkDataFactory.of(getTranslator());
        final List<Category> breadcrumbs = getBreadcrumbsForProduct(productProjection);
        return breadcrumbs.stream().map(categoryLinkDataFactory::create).collect(toList());
    }

    private PriceFinder getPriceFinder() {
        return userContext().priceFinder();
    }

    private PriceFormatter getPriceFormatter() {
        return userContext().priceFormatter();
    }

    private Translator getTranslator() {
        return userContext().translator();
    }

    private ProductVariant obtainProductVariantBySku(final String sku, final ProductProjection productProjection) {
        return productProjection.findVariantBySky(sku).orElseThrow(() -> ProductVariantNotFoundException.bySku(sku));
    }

    private Result notFoundAction() {
        return notFound();
    }

    private F.Promise<ProductProjection> fetchProduct(final String language, final String slug) {
        final Locale locale = new Locale(language);
        return productService.searchProductBySlug(locale, slug)
                .map(productOptional -> productOptional.orElseThrow(() -> ProductNotFoundException.bySlug(locale, slug)));

    }

    private List<ShopShippingRate> getShippingRates() {
        return shippingMethodService.getShippingRates(userContext().zone());
    }

    private List<Category> getBreadcrumbsForProduct(final ProductProjection product) {
        return product.getCategories().stream().findFirst()
                .map(categoryService::getBreadCrumbCategories)
                .orElse(Collections.<Category>emptyList());
    }

    private List<ProductThumbnailData> suggestionsToViewData(final List<ProductProjection> suggestions) {
        return suggestions.stream().map((product) -> ProductThumbnailDataFactory.of(getTranslator(), getPriceFinder(), getPriceFormatter()).create(product)).collect(toList());
    }
}